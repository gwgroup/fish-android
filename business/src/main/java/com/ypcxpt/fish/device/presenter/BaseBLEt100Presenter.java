package com.ypcxpt.fish.device.presenter;

import android.support.annotation.CallSuper;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.jakewharton.rx.ReplayingShare;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;

import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.ble.BLEClient;
import com.ypcxpt.fish.core.ble.BLEHelper;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.device.model.RabbitMqData;
import com.ypcxpt.fish.library.util.HexString;
import com.ypcxpt.fish.library.util.ListUtils;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.IView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.TARGET_CHARACTERISTIC_UUID_WRITE;

public class BaseBLEt100Presenter<T extends IView> extends BasePresenter<T> {

    public String chairName;
    public String macAddress;
    public UUID characteristicUuid;

    private RxBleDevice mBleDevice;

    private Observable<RxBleConnection> mConnectionObservable;

    private Disposable mConnectionDisposable;

    private PublishSubject<Boolean> mDisconnectTriggerSubject = PublishSubject.create();

    private static final long MIN_NOTIFY_MILLIS_TIME = 100;

    @Override
    public void init(IView view) {
        super.init(view);
        mBleDevice = BLEClient.get().getBleDevice(macAddress);
        mConnectionObservable = prepareConnectionObservable();
    }

    private Observable<RxBleConnection> prepareConnectionObservable() {
        return mBleDevice
                .establishConnection(false)
                .takeUntil(mDisconnectTriggerSubject)
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .compose(ReplayingShare.instance());
    }

    private boolean isConnected() {
        return mBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    public void startConnect() {
        if (isConnected()) {
            stopConnect();
        }

        mConnectionDisposable = mConnectionObservable
                .flatMapSingle(RxBleConnection::discoverServices)
                .flatMapSingle(rxBleDeviceServices -> rxBleDeviceServices.getCharacteristic(characteristicUuid))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        characteristic -> {
//                            Toaster.showLong("连接成功");
                            registerNotifies();
                        },
                        this::onConnectionFailure
                );
    }

    private void stopConnect() {
        if (mDisconnectTriggerSubject != null && mConnectionObservable != null && mConnectionDisposable != null && !mConnectionDisposable.isDisposed()) {
            mDisconnectTriggerSubject.onNext(true);
        }
    }

    @CallSuper
    public void onConnectionFailure(Throwable throwable) {
        if (mConnectionDisposable != null) {
            if (throwable.getMessage().contains("UNKNOWN") || throwable.getMessage().contains("GATT_ERR_UNLIKELY")) {
                Logger.e("onConnectionFailure", throwable.toString());
                mDisconnectTriggerSubject.onNext(true);
                startConnect();
            }
//            else {
//                Logger.e("onConnectionFailure_else", throwable.toString());
//                onGlobalFailure(throwable);
//            }

//            onGlobalFailure(throwable);
//        Toaster.showLong(throwable.getMessage().toString());
        }
    }

    private void onConnectionComplete() {
        Toaster.showLong("onConnectionComplete");
    }

    private void registerNotifies() {
        Logger.e("CCC", "registerNotifies:" + isConnected());
        if (isConnected()) {
            mConnectionObservable
                    .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUuid))
                    .doOnNext(notificationObservable -> getActivity().runOnUiThread(this::onNotificationRegisterSuccess))
                    .flatMap(notificationObservable -> notificationObservable)
                    .throttleFirst(MIN_NOTIFY_MILLIS_TIME, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onNotificationReceived, this::onNotificationRegisterFailure);
        } else {
            getActivity().dismissLoading();
        }
    }

    private void onNotificationRegisterSuccess() {
        Logger.e("CCC", "通知已注册成功");
    }

    private void onNotificationReceived(byte[] bytes) {
        String content = HexString.bytesToHex(bytes);
        onNotificationReceived(content);
    }

    @CallSuper
    public void onNotificationReceived(String content) {
        List<Integer> dataList = parse(content);
//        Logger.e("实时获取数据", dataList + "");
        //开始计算返回的数据最后一位的校验码
        int qrData = 0;
        for (int i = 0 ; i < dataList.size() - 1; i++) {
            qrData += dataList.get(i);
        }
        /**
         * 如果我计算的校验码取模后等于返回的校验码则通过
         */
        if ((qrData % 256) == dataList.get(dataList.size() -1)) {
            Calendar now = Calendar.getInstance();
            String yearStr = now.get(Calendar.YEAR) + "";
            int year = Integer.parseInt(yearStr.substring(2));
            String monthStr = (now.get(Calendar.MONTH) + 1) + "";
            int month = Integer.parseInt(monthStr);
            String dayStr = now.get(Calendar.DAY_OF_MONTH) + "";
            int day = Integer.parseInt(dayStr);
            String hourStr = now.get(Calendar.HOUR_OF_DAY) + "";
            int hour = Integer.parseInt(hourStr);
            String minuteStr = now.get(Calendar.MINUTE) + "";
            int minute = Integer.parseInt(minuteStr);
            sendNowStatus(1, year, month, day, hour, minute);
//            Logger.e("**", "pass");
        } else {
            Calendar now = Calendar.getInstance();
            String yearStr = now.get(Calendar.YEAR) + "";
            int year = Integer.parseInt(yearStr.substring(2));
            String monthStr = (now.get(Calendar.MONTH) + 1) + "";
            int month = Integer.parseInt(monthStr);
            String dayStr = now.get(Calendar.DAY_OF_MONTH) + "";
            int day = Integer.parseInt(dayStr);
            String hourStr = now.get(Calendar.HOUR_OF_DAY) + "";
            int hour = Integer.parseInt(hourStr);
            String minuteStr = now.get(Calendar.MINUTE) + "";
            int minute = Integer.parseInt(minuteStr);
            sendNowStatus(0, year, month, day, hour, minute);
//            Logger.e("**", "error");
        }
    }

    /**
     * 我这边回指令
     * @param code 1正确0错误
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     */
    protected void sendNowStatus(int code, int year, int month, int day, int hour, int minute) {
//        Logger.e("**", code + "-" + year + "-" + month + "-" + day + "-" + hour + "-" + minute);
        String strData = BLEHelper.sendNowStatusDataWithT100(code, year, month, day, hour, minute);

        byte[] data = HexString.hexToBytes(strData);
        if (isConnected()) {
            mConnectionObservable
                    .firstOrError()
                    .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(TARGET_CHARACTERISTIC_UUID_WRITE), data))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            bytes -> onWriteSuccess(code),
                            this::onWriteFailure
                    );
        }
    }

    /**
     * 将接收到的原始数据转化为int数组
     * @param text 原始数据
     * @return 想要的数据 最后一位是前面相加的和即校验码
     */
    private List<Integer> parse(String text) {
        if (StringUtils.isTrimEmpty(text)) return null;

        int VALIDATE_SECTIONS = text.length()/2;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < VALIDATE_SECTIONS; i++) {
//            if (i == 0 || i == 1 || i == VALIDATE_SECTIONS - 1) {
//                continue;
//            }
            String section = text.substring(i * 2, i * 2 + 2);
            list.add(HexString.hexStringToInt(section));
        }

        ListUtils.printHexString(list);
        return list;
    }

    @CallSuper
    public void onNotificationRegisterFailure(Throwable throwable) {
        if (mConnectionDisposable != null ) {
            if (throwable.getMessage().contains("UNKNOWN") || throwable.getMessage().contains("GATT_ERR_UNLIKELY")) {
                Logger.e("notify_UNKNOWN", throwable.toString());
                mDisconnectTriggerSubject.onNext(true);
                startConnect();
            } else {
                Logger.e("notify_throwable", throwable.toString());
                onGlobalFailure(throwable);
            }
//            onGlobalFailure(throwable);
        }
    }

    public void writeData(DeviceAction deviceAction) {
        int code = deviceAction.code;
        if (deviceAction.longClick) {
            writeData(code);
            Logger.d("CCC", deviceAction.name + "  " + deviceAction.code);
            ThreadHelper.postDelayed(() -> {
                Logger.d("CCC", deviceAction.name + "  " + deviceAction.code);
                writeData(code);
            }, 1000);
        } else {
            writeData(code);
        }
    }

    /**
     * @param code 设备的指令码
     */
    protected void writeData(int code) {
        if (code < 0) return;

        sendCode(code);//记录发的指令到队列

        String strData = BLEHelper.wrapInputDataWithT100(code);
        Logger.e("T100", code + "发指令-->" + strData);
        byte[] data = HexString.hexToBytes(strData);

        if (isConnected()) {
            mConnectionObservable
                    .firstOrError()
                    .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(TARGET_CHARACTERISTIC_UUID_WRITE), data))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            bytes -> onWriteSuccess(code),
                            this::onWriteFailure
                    );
        }

    }

    @CallSuper
    public void onWriteSuccess(int code) {
        Logger.i("CCC", "写入成功");
    }

    @CallSuper
    public void onWriteFailure(Throwable throwable) {
        Logger.i("CCC", throwable.getMessage().toString());
    }

    public void onGlobalFailure(Throwable throwable) {
    }

    @Override
    public void onDestroy() {
//        stopConnect();
        super.onDestroy();
        if (subscribeThread != null) {
            subscribeThread.interrupt();
        }
    }

    Thread subscribeThread;
    private void sendCode(int code) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);

        RabbitMqData rabbitMqData = new RabbitMqData();
        List<RabbitMqData.LogsBean> logsBeans = new ArrayList<>();
        RabbitMqData.LogsBean logsBean = new RabbitMqData.LogsBean();
        logsBean.setIdentifier(macAddress);
        logsBean.setAction_time(dateNowStr);
        logsBean.setAction_code(code);
        logsBean.setData("");
        logsBeans.add(logsBean);
        rabbitMqData.setUser_id(SPHelper.getString("USER_ID"));
        rabbitMqData.setLogs(logsBeans);

        ConnectionFactory factory = new ConnectionFactory();
//        //设置RabbitMQ相关信息
        factory.setHost("device.reead.net");//主机地址：
        factory.setPort(5672);// 端口号
        factory.setUsername("rh");// 用户名
        factory.setPassword("2q018reead");// 密码
        factory.setVirtualHost("rh");
        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个新的连接
                    Connection connection = factory.newConnection();
                    //创建一个通道
                    Channel channel = connection.createChannel();

                    //发送消息到队列中
                    String message = new Gson().toJson(rabbitMqData);
                    Logger.e("信息-->", message);
                    channel.basicPublish("", "device_action", false,null, message.getBytes("UTF-8"));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //此时已在主线程中，可以更新UI了
//                            Toaster.showShort("发送成功");
                            Logger.e("发送指令-->", "" + code);
                        }
                    });

                    //关闭通道和连接
                    channel.close();
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
        );
        subscribeThread.start();
    }
}
