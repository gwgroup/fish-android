package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.CommentForMeContract;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.view.fragment.CommentForMeFragment;

import io.reactivex.Flowable;

public class CommentForMePresenter extends BasePresenter<CommentForMeContract.View> implements CommentForMeContract.Presenter {
    private DataSource mDS;

    public CommentForMePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void skipCollectionDetail(CommentInfo.RowsBean rowsBean) {
    }

    @Override
    public void getCommentForMeList(String pageIndex, String pageNum) {
        Flowable<CommentInfo> source = mDS.getMyComments(pageIndex, pageNum);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(CommentForMeFragment.isRefresh)
                .showNoNetWarning(true).onSuccess(commentInfo -> {
            Logger.d("CCC", "commentInfo-->" + commentInfo.toString());
            CommentForMeFragment.isRefresh = true;
            mView.showCommentForMeList(commentInfo);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
