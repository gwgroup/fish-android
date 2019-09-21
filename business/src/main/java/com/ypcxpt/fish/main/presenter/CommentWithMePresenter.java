package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.CommentWithMeContract;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.view.fragment.CommentForOtherFragment;

import io.reactivex.Flowable;

public class CommentWithMePresenter extends BasePresenter<CommentWithMeContract.View> implements CommentWithMeContract.Presenter {
    private DataSource mDS;

    public CommentWithMePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getCommentWithMeList(String pageIndex, String pageNum) {
        Flowable<CommentInfo> source = mDS.getCommentsWithMe(pageIndex, pageNum);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(CommentForOtherFragment.isRefresh)
                .showNoNetWarning(true).onSuccess(commentWithmeInfo -> {
            Logger.d("CCC", "commentWithmeInfo-->" + commentWithmeInfo.toString());
            CommentForOtherFragment.isRefresh = true;
            mView.showCommentWithMeList(commentWithmeInfo);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }
}
