package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.CommentInfo;

public interface CommentForMeContract {
    interface View extends IView {
        void showCommentForMeList(CommentInfo collectionInfo);
    }

    interface Presenter extends IPresenter {
        //跳转收藏详情进行操作
        void skipCollectionDetail(CommentInfo.RowsBean rowsBean);
        void getCommentForMeList(String pageIndex, String pageNum);
    }
}
