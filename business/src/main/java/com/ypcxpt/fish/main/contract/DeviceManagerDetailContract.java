package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.login.model.UserProfile;

public interface DeviceManagerDetailContract {
    interface View extends IView {
        void onUpdateSuccess(UserProfile newProfile);
    }

    interface Presenter extends IPresenter {
        //解绑移除设备
        void removeDevice(NetDevice device);
        //重命名设备
        void renameDevice(NetDevice device);
    }
}
