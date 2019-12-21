package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.main.contract.DeviceManagerContract;

public class DeviceManagerPresenter extends BasePresenter<DeviceManagerContract.View> implements DeviceManagerContract.Presenter {

    private DataSource mDS;

    public DeviceManagerPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getDevices() {
//        Flowable<List<NetDevice>> source = mDS.getDevices();
//        fetch(source).onSuccess(devices -> {
//            Logger.d("CCC", "devices-->" + devices.toString());
//            mView.showDevices(devices);
//        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }
}
