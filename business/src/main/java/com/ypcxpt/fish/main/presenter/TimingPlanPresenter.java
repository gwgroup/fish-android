package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.main.contract.TimingPlanContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.model.IoTrigger;

import org.greenrobot.eventbus.EventBus;

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
    public void getAllPlans(String mac) {
        Flowable<List<IoPlan>> source = mDS.getAllPlan(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(ioPlans -> {
                    Logger.d("CCC", "ioPlans-->" + ioPlans.toString());
                    mView.showIoPlans(ioPlans);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void openPlan(String mac, String planId) {
        Flowable<Object> source = mDS.openPlan(mac, planId);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "启用计划成功");
                    ThreadHelper.postDelayed(() -> getAllPlans(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void closePlan(String mac, String planId) {
        Flowable<Object> source = mDS.closePlan(mac, planId);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "禁用计划成功");
                    ThreadHelper.postDelayed(() -> getAllPlans(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getAllTriggers(String mac) {
        Flowable<List<IoTrigger>> source = mDS.getAllTrigger(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(ioTrigger -> {
                    Logger.d("CCC", "ioTrigger-->" + ioTrigger.toString());
                    mView.showIoTriggers(ioTrigger);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void openTrigger(String mac, String planId) {
        Flowable<Object> source = mDS.openTrigger(mac, planId);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "启用触发任务成功");
                    ThreadHelper.postDelayed(() -> getAllTriggers(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void closeTrigger(String mac, String planId) {
        Flowable<Object> source = mDS.closeTrigger(mac, planId);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "禁用触发任务成功");
                    ThreadHelper.postDelayed(() -> getAllTriggers(mac), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
