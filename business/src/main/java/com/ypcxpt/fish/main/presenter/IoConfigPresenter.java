package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
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
}
