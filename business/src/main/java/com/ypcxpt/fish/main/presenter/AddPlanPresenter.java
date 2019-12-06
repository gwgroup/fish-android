package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.AddPlanContract;
import com.ypcxpt.fish.main.model.IoInfo;

import java.util.List;

import io.reactivex.Flowable;

public class AddPlanPresenter extends BasePresenter<AddPlanContract.View> implements AddPlanContract.Presenter {
    private DataSource mDS;

    public String DEVICE_MAC;

    public AddPlanPresenter() {
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
                    Logger.d("定时计划", "ioInfos-->" + ioInfos.toString());
                    mView.showIoInfos(ioInfos);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void commitOpinion(String content) {
        Flowable<Object> source = mDS.feedback(content);
        fetch(source).onSuccess(o -> {
            Toaster.showShort("提交成功，感谢您的反馈");
            mView.onCommitSuccess();
        }).start();
    }
}
