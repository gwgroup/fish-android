package com.ypcxpt.fish.login.presenter;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.login.contract.LoginContract;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;

import io.reactivex.Flowable;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private DataSource mDS;

    public LoginPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void getVerifyCode(String phoneNo) {
        Flowable<Object> source = mDS.getVerifyCode(phoneNo);
        fetch(source).onSuccess(o -> {
            Toaster.showLong("验证码发送成功");
        }).start();
    }

    @Override
    public void login(String phoneNo, String verifyCode) {
        Flowable<LoginResult> source = mDS.login(phoneNo, verifyCode);
        fetch(source).onSuccess(loginResult -> {
            if (loginResult != null && !StringUtils.isTrimEmpty(loginResult.token)) {
                AppData.setToken(loginResult.token);
                mView.afterLoginSuccess();
            }
            Logger.d("CCC", "onSuccess:" + loginResult.toString());
        }).start();
    }

    @Override
    public void getUser() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        fetch(source).onSuccess(userProfile -> {
            Logger.d("CCC", userProfile.toString());
            mView.afterLoginSuccessGetUser(userProfile);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getUserProfile() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        fetch(source).onSuccess(userProfile -> {
            Logger.d("CCC", userProfile.toString());
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        Flowable<UserProfile> source = mDS.updateUserProfile(userProfile);
        fetch(source).onSuccess(newProfile -> {
            Logger.d("CCC", newProfile.toString());
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void addDevice(NetDevice device) {
        Flowable<Object> source = mDS.addDevice(device);
        fetch(source).onSuccess(o -> {
            Toaster.showLong("设备添加成功");
        }).start();
    }

    @Override
    public void removeDevice(NetDevice device) {
        Flowable<Object> source = mDS.removeDevice(device);
        fetch(source).onSuccess(o -> {
            Toaster.showLong("设备删除成功");
        }).start();
    }

}
