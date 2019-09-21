package com.ypcxpt.fish.device.presenter;

import android.support.annotation.CallSuper;

import com.jakewharton.rx.ReplayingShare;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.trello.rxlifecycle2.android.ActivityEvent;

import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.ble.BLEClient;
import com.ypcxpt.fish.core.ble.BLEHelper;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.library.util.HexString;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.IView;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.TARGET_CHARACTERISTIC_UUID_WRITE;

public class BaseBLEPresenter<T extends IView> extends BasePresenter<T> {

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
        Logger.d("CCC", "registerNotifies:" + isConnected());
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
        Logger.d("CCC", "通知已注册成功");
//        Toaster.showLong("通知已注册成功");
    }

    private void onNotificationReceived(byte[] bytes) {
        String content = HexString.bytesToHex(bytes);
        onNotificationReceived(content);
    }

    @CallSuper
    public void onNotificationReceived(String content) {
//        Logger.d("CCC收到数据", content);
        if ("HOME-6".equals(chairName)) {
            //接收到数据后回复00指令
            String strData = "ab55160016";
            byte[] data = HexString.hexToBytes(strData);
            if (isConnected()) {
                mConnectionObservable
                        .firstOrError()
                        .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(TARGET_CHARACTERISTIC_UUID_WRITE), data))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                bytes -> onWriteSuccess(0),
                                this::onWriteFailure
                        );
            }
        }

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
        Logger.e("CCC", chairName + "-->" + code);
        if (code <= 0) return;

        if ("HOME-8".equals(chairName)) {
            String strData = BLEHelper.wrapInputData(code);
            Logger.e("HOME-8", "发指令-->" + strData);
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
        } else if ("HOME-6".equals(chairName)) {
            String strData = BLEHelper.wrapInputDataWithShellchair(code);
            Logger.e("HOME-6", "发指令-->" + strData);
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

    }

    @CallSuper
    public void onWriteSuccess(int code) {
//        Toaster.showLong("写入成功");
    }

    @CallSuper
    public void onWriteFailure(Throwable throwable) {
        Toaster.showLong("暂无反应");
        Logger.d("CCC", throwable.getMessage().toString());
    }

    public void onGlobalFailure(Throwable throwable) {
    }

    @Override
    public void onDestroy() {
        stopConnect();
        super.onDestroy();
    }
}
