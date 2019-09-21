package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.FeedbackContract;

import io.reactivex.Flowable;

public class FeedbackPresenter extends BasePresenter<FeedbackContract.View> implements FeedbackContract.Presenter {
    private DataSource mDS;

    public FeedbackPresenter() {
        mDS = new DataRepository();
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
