package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.NotificationContract;
import com.ypcxpt.fish.main.model.NotificationInfo;

import io.reactivex.Flowable;

public class NotificationPresenter extends BasePresenter<NotificationContract.View> implements NotificationContract.Presenter {
    private DataSource mDS;

    public NotificationPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getNotifications(String pageIndex, String pageNum) {
        Flowable<NotificationInfo> source = mDS.getNotifications(pageIndex, pageNum);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true).onSuccess(notificationInfo -> {
            Logger.d("CCC", "notificationInfo-->" + notificationInfo.toString());
            mView.showNotifications(notificationInfo);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
