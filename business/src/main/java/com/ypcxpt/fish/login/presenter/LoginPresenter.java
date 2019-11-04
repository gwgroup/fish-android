package com.ypcxpt.fish.login.presenter;

import com.google.gson.Gson;
import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.login.contract.LoginContract;
import com.ypcxpt.fish.login.model.LoginInfo;
import com.ypcxpt.fish.login.model.UserProfile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.reactivex.Flowable;

import static com.ypcxpt.fish.BaseUrlConstant.BASE_URL;

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
//        Flowable<LoginResult> source = mDS.login(phoneNo, verifyCode);
//        fetch(source).onSuccess(loginResult -> {
//            if (loginResult != null && !StringUtils.isTrimEmpty(loginResult.token)) {
//                AppData.setToken(loginResult.token);
//                mView.afterLoginSuccess();
//            }
//            Logger.d("CCC", "onSuccess:" + loginResult.toString());
//        }).start();

        RequestParams params = new RequestParams(BASE_URL + "api/user/login");
        params.addParameter("mobile", phoneNo);
        params.addParameter("vali_code", verifyCode);
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("登录", result);
                Gson gson = new Gson();
                LoginInfo loginInfo = gson.fromJson(result, LoginInfo.class);
                if (loginInfo.getCode() == 1000) {
                    //获取到token保存到本地
                    AppData.setToken(loginInfo.getData());
                    mView.afterLoginSuccess();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
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
    public void removeDevice(NetDevice device) {
        Flowable<Object> source = mDS.removeDevice(device);
        fetch(source).onSuccess(o -> {
            Toaster.showLong("设备删除成功");
        }).start();
    }

}
