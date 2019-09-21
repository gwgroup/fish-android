package com.ypcxpt.fish.main.view.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.contract.FeedbackContract;
import com.ypcxpt.fish.main.presenter.FeedbackPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.EDIT_FEEDBACK)
public class EditFeedbackActivity extends BaseActivity implements FeedbackContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_feedback) EditText et_feedback;

    private FeedbackContract.Presenter mPresenter;

    @Override
    protected int layoutResID() {
        return R.layout.activiy_edit_feedback;
    }

    @Override
    protected void initData() {
        mPresenter = new FeedbackPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        tv_title.setText("意见反馈");
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.tv_commit)
    public void onBackSave() {
        String content = ViewHelper.getText(et_feedback);
        if (StringUtils.isTrimEmpty(content)) {
            Toaster.showShort("请输入您要反馈的问题");
            return;
        }
        mPresenter.commitOpinion(content);
    }

    @Override
    public void onCommitSuccess() {
        onBackPressed();
    }
}
