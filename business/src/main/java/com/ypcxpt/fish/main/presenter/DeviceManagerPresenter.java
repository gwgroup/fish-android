package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.adapter.DeviceAdapter;
import com.ypcxpt.fish.main.contract.DeviceManagerContract;

import java.util.List;

import io.reactivex.Flowable;

public class DeviceManagerPresenter extends BasePresenter<DeviceManagerContract.View> implements DeviceManagerContract.Presenter {
    private DeviceAdapter mAdapter;

    private DataSource mDS;

    public DeviceManagerPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void skipDetail(NetDevice device) {
        Router.build(Path.Main.DEVICE_MANAGER_DETAIL)
                .withParcelable("mDevice", device)
                .navigation(getActivity());
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
