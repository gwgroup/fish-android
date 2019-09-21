package com.ypcxpt.fish.login.contract;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.login.model.UserProfile;

public interface LoginContract {
    interface View extends IView {
        void afterLoginSuccess();
        //登录成功之后获取用户信息
        void afterLoginSuccessGetUser(UserProfile userProfile);
    }

    interface Presenter extends IPresenter {
        void getVerifyCode(String phoneNo);

        void login(String phoneNo, String verifyCode);

        //登录成功之后获取用户信息
        void getUser();

        void getUserProfile();

        void updateUserProfile(UserProfile userProfile);

        void addDevice(NetDevice device);

        void removeDevice(NetDevice device);
    }
}
