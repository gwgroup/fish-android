package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

public interface FeedbackContract {
    interface View extends IView {
        void onCommitSuccess();
    }

    interface Presenter extends IPresenter {
        void commitOpinion(String content);
    }
}
