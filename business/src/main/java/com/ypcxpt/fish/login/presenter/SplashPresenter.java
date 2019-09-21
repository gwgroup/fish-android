package com.ypcxpt.fish.login.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.login.event.SplashEvent;
import com.ypcxpt.fish.login.model.UserProfile;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Flowable;

public class SplashPresenter {
    private DataSource mDS;

    public SplashPresenter() {
        mDS = new DataRepository();
    }

    /**
     * 如果token过期，以下3个回调都不会走，而是底层拦截.
     */
    public void checkToken() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        new Fetcher<>(source)
                .onSuccess(userProfile -> postTokenEvent(true, userProfile))
                .onBizError(bizMsg -> postTokenEvent(false, null))
                .onError(t -> postTokenEvent(false, null
                ))
                .start();
    }

    private void postTokenEvent(boolean isRequestSuccess, UserProfile userProfile) {
        EventBus.getDefault().post(new SplashEvent(isRequestSuccess, userProfile));
    }
}
