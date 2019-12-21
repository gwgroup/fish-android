package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.UserProfileContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Flowable;

public class UserProfilePresenter extends BasePresenter<UserProfileContract.View> implements UserProfileContract.Presenter {
    private DataSource mDS;

    public UserProfilePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void openDataFetching() {
        /* 改从Splash传进来，因为存在可能：Splash拿到User信息，但是该页面刷新失败的情况 */
//        getUserProfile();
    }

    private void getUserProfile() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        fetch(source)
                .onSuccess(userProfile -> {
                    Logger.d("CCC", userProfile == null ? "空用户信息" : userProfile.toString());
                    refreshUerProfile(userProfile);
//                    mView.updateUser(userProfile);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    private void refreshUerProfile(UserProfile userProfile) {
        EventBus.getDefault().post(new OnProfileUpdatedEvent(userProfile));
    }

    @Override
    public void refreshDevice(UserProfile userProfile) {
//        EventBus.getDefault().post(new OnGetScenesEvent());
    }

    /**
     * 退出登录
     * @param token
     */
    @Override
    public void logout(String token) {
        Logger.i("UserProfilePresenter", "token-->" + token);
        AppData.setToken("");
        mView.afterLogoutSuccess();
    }

    @Override
    public void getDevices() {
    }

    @Override
    public void refreshUser() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true)
                .onSuccess(userProfile -> {
                    Logger.d("CCC", userProfile == null ? "空用户信息" : userProfile.toString());
                    EventBus.getDefault().post(new OnProfileUpdatedEvent(userProfile));
                    EventBus.getDefault().post(new OnGetScenesEvent());
//                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnProfileUpdatedEvent(userProfile)), 500);
//                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

}
