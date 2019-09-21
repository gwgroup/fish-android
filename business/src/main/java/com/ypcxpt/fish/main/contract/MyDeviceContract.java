package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.List;

public interface MyDeviceContract {
    interface View extends IView {
        void onStopScan();

        void onScanFailure();

        void onGetWhetherResult(WeatherInfo weatherInfo);

        void showDevices(List<NetDevice> devices);
    }

    interface Presenter extends IPresenter {
        void checkBluetoothState();

        void startScan(boolean continueScanning);

        void connectServices(NetDevice scanResult);

        void addDevice(NetDevice device);

        //扫描二维码
        void startCodeScan(String name, String mac);
        //解绑移除设备
        void removeDevice(NetDevice device);
        //重命名设备
        void renameDevice(NetDevice device);

        void getDevices();

        //跳转设备详情进行操作
        void skipDetail(NetDevice device);
    }
}
