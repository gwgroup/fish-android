package com.ypcxpt.fish.login.presenter;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.login.contract.BindContract;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;

import io.reactivex.Flowable;

public class BindPresenter extends BasePresenter<BindContract.View> implements BindContract.Presenter {
    private DataSource mDS;

    public BindPresenter() {
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
    public void bind(String phoneNo, String verifyCode, String openid) {
        Flowable<LoginResult> source = mDS.bindPhone(phoneNo, verifyCode, openid);
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

}
