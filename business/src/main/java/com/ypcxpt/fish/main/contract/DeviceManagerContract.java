package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

import java.util.List;

public interface DeviceManagerContract {
    interface View extends IView {
        void showDevices(List<NetDevice> devices);
    }

    interface Presenter extends IPresenter {
        //跳转设备详情进行操作
        void skipDetail(NetDevice device);
        void getDevices();
    }
}
