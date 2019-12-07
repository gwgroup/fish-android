package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.AddTriggerContract;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.PlanParam;
import com.ypcxpt.fish.main.model.TriggerParam;

import java.util.List;

import io.reactivex.Flowable;

public class AddTriggerPresenter extends BasePresenter<AddTriggerContract.View> implements AddTriggerContract.Presenter {
    private DataSource mDS;

    public String DEVICE_MAC;

    public AddTriggerPresenter() {
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
                    Logger.d("触发任务", "ioInfos-->" + ioInfos.toString());
                    mView.showIoInfos(ioInfos);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void addPlan(String mac, TriggerParam planParam) {
        Flowable<Object> source = mDS.addTrigger(mac, planParam);
        fetch(source).onSuccess(o -> {
            Toaster.showShort("添加触发任务成功");
            mView.onCommitSuccess();
        }).start();
    }

    @Override
    public void editPlan(String mac, TriggerParam planParam) {
        Flowable<Object> source = mDS.editTrigger(mac, planParam);
        fetch(source).onSuccess(o -> {
            Toaster.showShort("修改触发任务成功");
            mView.onCommitSuccess();
        }).start();
    }

    @Override
    public void deletePlan(String mac, String planId) {
        Flowable<Object> source = mDS.deleteTrigger(mac, planId);
        silenceFetch(source)
                .onSuccess(o -> {
                    Toaster.showShort("删除触发任务成功");
                    mView.onCommitSuccess();
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
