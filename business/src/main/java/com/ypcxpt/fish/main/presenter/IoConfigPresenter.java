package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.main.contract.IoConfigContract;
import com.ypcxpt.fish.main.model.IoInfo;

import java.util.List;

import io.reactivex.Flowable;

public class IoConfigPresenter extends BasePresenter<IoConfigContract.View> implements IoConfigContract.Presenter {

    public String DEVICE_MAC;

    private DataSource mDS;

    public IoConfigPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void openDataFetching() {
        getIoinfos(DEVICE_MAC);
    }

    private void getIoinfos(String mac) {
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void enableIO(String mac, String code) {
        Flowable<Object> source = mDS.enableIO(mac, code);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "启用IO成功");
                    ThreadHelper.postDelayed(() -> getIoinfos(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void disableIO(String mac, String code) {
        Flowable<Object> source = mDS.disableIO(mac, code);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "禁用IO成功");
                    ThreadHelper.postDelayed(() -> getIoinfos(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void renameIO(String mac, String code, String name) {
        Flowable<Object> source = mDS.renameIO(mac, code, name);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "重命名IO成功");
                    ThreadHelper.postDelayed(() -> getIoinfos(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void setIOPower(String mac, String code, int power) {
        Flowable<Object> source = mDS.setPowerIO(mac, code, power);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "设置IO功耗成功");
                    ThreadHelper.postDelayed(() -> getIoinfos(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void calibrationFeeder(String mac, String code, double feeder) {
        Flowable<Object> source = mDS.calibrationFeederIO(mac, code, feeder);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "校准投喂机成功");
                    ThreadHelper.postDelayed(() -> getIoinfos(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
