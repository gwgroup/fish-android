package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.DeviceManagerDetailContract;

import io.reactivex.Flowable;

public class DeviceManagerDetailPresenter extends BasePresenter<DeviceManagerDetailContract.View> implements DeviceManagerDetailContract.Presenter {
    private DataSource mDS;

    public DeviceManagerDetailPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void removeDevice(NetDevice device) {
//        Flowable<Object> source = mDS.removeDevice(device);
//        silenceFetch(source)
//                .onSuccess(o -> {
//                    Logger.d("CCC", "移除成功");
//                    getUser();
//                })
//                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }

    @Override
    public void renameDevice(NetDevice device) {
//        Flowable<Object> source = mDS.renameDevice(device);
//        silenceFetch(source)
//                .onSuccess(o -> {
//                    Logger.d("CCC", "重命名成功");
//                    getUser();
//                })
//                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }

    public void getUser() {
        Flowable<UserProfile> source = mDS.getUserProfile();
        fetch(source).onSuccess(userProfile -> {
            Logger.d("CCC", userProfile.toString());
            mView.onUpdateSuccess(userProfile);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
