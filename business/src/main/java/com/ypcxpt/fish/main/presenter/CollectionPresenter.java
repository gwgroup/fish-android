package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.CollectionContract;
import com.ypcxpt.fish.main.model.CollectionInfo;

import io.reactivex.Flowable;

public class CollectionPresenter extends BasePresenter<CollectionContract.View> implements CollectionContract.Presenter {
    private DataSource mDS;

    public CollectionPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void skipCollectionDetail(CollectionInfo.RowsBean rowsBean) {
    }

    @Override
    public void getCollections(String pageIndex, String pageNum) {
        Flowable<CollectionInfo> source = mDS.getCollections(pageIndex, pageNum);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true).onSuccess(collectionInfo -> {
            Logger.d("CCC", "devices-->" + collectionInfo.toString());
            mView.showCollections(collectionInfo);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
