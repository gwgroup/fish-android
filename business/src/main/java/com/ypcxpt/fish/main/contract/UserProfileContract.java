package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.login.model.UserProfile;

public interface UserProfileContract {
    interface View extends IView {
        //点击退出登录之后在此方法中跳转至登录页
        void afterLogoutSuccess();
    }

    interface Presenter extends IPresenter {
        void refreshDevice(UserProfile userProfile);

        //退出登录
        void logout(String token);
        //获取设备信息
        void getDevices();

        //刷新用户信息
        void refreshUser();
    }
}
