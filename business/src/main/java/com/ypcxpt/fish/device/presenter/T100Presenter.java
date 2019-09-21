package com.ypcxpt.fish.device.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.ble.input.T100DataParser;
import com.ypcxpt.fish.core.ble.output.data.T100ManualActionData;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.contract.T100Contract;
import com.ypcxpt.fish.device.model.DataHistory;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.IView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;

import static com.ypcxpt.fish.core.ble.output.data.BaseActionData.ACTION_CODE_POWER;

public class T100Presenter extends BaseBLEt100Presenter<T100Contract.View> implements T100Contract.Presenter {
    /* 是否开机 */
    private boolean isPowerOn;

    private DataSource mDS;

    public T100Presenter() {
        mDS = new DataRepository();
    }

    @Override
    public void init(IView view) {
        super.init(view);
    }

    @Override
    public void openDataFetching() {
        ThreadHelper.postDelayed(() -> startConnect(), 600);
    }

    @Override
    protected void writeData(int code) {
        if (ACTION_CODE_POWER == code) {
            super.writeData(code);
        } else if (isPowerOn) {
            super.writeData(code);
        }
    }

    @Override
    public void onWriteSuccess(int code) {
        super.onWriteSuccess(code);
    }

    @Override
    public void onNotificationReceived(String content) {
        super.onNotificationReceived(content);
        T100DataParser parser = T100DataParser.get();
        List<Integer> data = parser.parse(content);

        /**
         * 数据类型为1代表实时数据，用于显示界面UI是否高亮
         */
        if (data.get(2) == 1) {
//            Logger.e("实时获取数据", data + "");
            /* 机器状态 */
            readDeviceState(data);
            /* 时间 */
            readTime(parser, data);
        } else if (data.get(2) == 3) {
            //数据类型为3代表历史数据
            readHistory(data);
        }
    }

    List<Integer> dataPin = new ArrayList<>();
    /**
     * 统计历史数据
     * @param data
     */
    private void readHistory(List<Integer> data) {
        Logger.e("历史数据", data + "");
        if (data.get(3) == 1) {//是起始包
            dataPin.addAll(data);
        } else if (data.get(3) == 0) {//是中间包
            if (dataPin != null &&  dataPin.size() > 0) {
                if (data.get(data.size() - 1) == 0) {
                    for (int i = 4; i < data.size(); i++) {
                        dataPin.add(data.get(i));
                    }
                    Logger.e("拼接的数据", dataPin + "");

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
                    sendNowStatus(4, year, month, day, hour, minute);

                    //调用接口
                    uploadHistory(dataPin + "");
//                ACache aCache = ACache.get(getActivity());
////                dataPin = (ArrayList<String>) aCache.getAsObject("dataPin");
//                aCache.put("dataPin", dataPin + "");

                    dataPin.clear();
                } else {
                    for (int i = 4; i < data.size(); i++) {
                        dataPin.add(data.get(i));
                    }
                }
            }
        }
    }

    private void uploadHistory(String data) {
        List<DataHistory> historyList = new ArrayList<>();
        DataHistory dataHistory = new DataHistory();
        dataHistory.setIdentifier(macAddress);
        dataHistory.setAction_time(null);
        dataHistory.setAction_code(-1);
        dataHistory.setData(data);
        historyList.add(dataHistory);

        actionLog(historyList);
    }

    private void actionLog(List<DataHistory> historyList) {
        Flowable<Object> source = mDS.deviceActionLog(historyList);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true)
                .onSuccess(o -> {
                    Logger.i("数据日志", "记录一次");
                }).start();
    }

    /* 机器状态 */
    private void readDeviceState(List<Integer> data) {
        if (data.get(3) == 1) {
            isPowerOn = true;
        } else {
            isPowerOn = false;
        }

        if (mView != null) {
            mView.onConnectSuccess();

            mView.displayDeviceState(data.get(3));//开关机
            mView.displayNeckPositiveAndNegative(data.get(4));//颈部按摩正反
            mView.displayNeckUpAndDown(data.get(5));//颈部按摩上下
            mView.displayAutoMode(data.get(6));//背部按摩+自动模式
            mView.displayAir(data.get(7));//气压档位
            mView.displayHeat(data.get(8));//加热
        }
    }

    /* 时间 */
    private void readTime(T100DataParser parser, List<Integer> data) {
        String time = parser.getTime(data);
        mView.displayTime(time);
    }

    @Override
    public void togglePower() {
        if (isPowerOn) {
            writeData(T100ManualActionData.getPower().get(0));
        } else {
            writeData(T100ManualActionData.getPower().get(1));
        }
    }

    @Override
    public void onGlobalFailure(Throwable throwable) {
        super.onGlobalFailure(throwable);
        if (mView != null) {
            mView.onConnectFailure();
        }
    }

    @Override
    public void useDevice(String mac) {
        Flowable<Object> source = mDS.getControlDevice(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true)
                .onSuccess(o -> {
            Logger.i("使用设备", "记录一次");
        }).start();
    }
}
