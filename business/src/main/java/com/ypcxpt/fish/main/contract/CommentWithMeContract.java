package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.CommentInfo;

public interface CommentWithMeContract {
    interface View extends IView {
        void showCommentWithMeList(CommentInfo collectionInfo);
    }

    interface Presenter extends IPresenter {
        void getCommentWithMeList(String pageIndex, String pageNum);
    }
}
