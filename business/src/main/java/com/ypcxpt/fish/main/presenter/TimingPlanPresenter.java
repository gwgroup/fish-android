package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.TimingPlanContract;

import java.util.List;

import io.reactivex.Flowable;

public class TimingPlanPresenter extends BasePresenter<TimingPlanContract.View> implements TimingPlanContract.Presenter {

    private DataSource mDS;

    public TimingPlanPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void openDataFetching() {
        super.openDataFetching();
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
    public void onDestroy() {
        super.onDestroy();
    }
}
