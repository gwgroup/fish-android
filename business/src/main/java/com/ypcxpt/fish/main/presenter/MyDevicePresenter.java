package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.model.IoInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Flowable;

public class MyDevicePresenter extends BasePresenter<MyDeviceContract.View> implements MyDeviceContract.Presenter {
    private DataSource mDS;

    public MyDevicePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void addScenes(String mac, String name) {
        Flowable<Object> source = mDS.addScenes(mac, name);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "添加场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void removeScenes(String mac) {
        Flowable<Object> source = mDS.removeScenes(mac);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "移除场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void renameScenes(String mac, String name) {
        Flowable<Object> source = mDS.renameScenes(mac, name);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "重命名场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getScenes() {
        Flowable<List<Scenes>> source = mDS.getScenes();
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
        .onSuccess(scenes -> {
            Logger.d("CCC", "scenes-->" + scenes.toString());
            mView.showScenes(scenes);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getIoinfos(String mac) {
        Flowable<List<IoInfo>> source = mDS.getIoInfo(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(ioInfos -> {
                    Logger.d("CCC", "ioInfos-->" + ioInfos.toString());
                    mView.showIoInfos(ioInfos);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void openIO(String mac, String code, int duration) {
        Flowable<Object> source = mDS.openIO(mac, code, duration);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(o -> {
                    Logger.d("CCC", "打开成功");
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void closeIO(String mac, String code) {
        Flowable<Object> source = mDS.closeIO(mac, code);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(o -> {
                    Logger.d("CCC", "关闭成功");
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void skipDetail(NetDevice device) {
        Router.build(Path.Main.DEVICE_MANAGER_DETAIL)
                .withParcelable("mDevice", device)
                .navigation(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
