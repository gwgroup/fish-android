package com.ypcxpt.fish.main.view.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.StringUtils;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.contract.AddPlanContract;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.presenter.AddPlanPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.ADD_PLAN)
public class AddPlanActivity extends BaseActivity implements AddPlanContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_feeder) EditText et_feeder;

    private AddPlanContract.Presenter mPresenter;

    @Autowired(name = "PLAN_TYPE")
    public int PLAN_TYPE;
    @Autowired(name = "IO_PLAN")
    public IoPlan ioPlan;

    @Override
    protected int layoutResID() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initData() {
        mPresenter = new AddPlanPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        if (PLAN_TYPE == 1) {
            tv_title.setText("添加定时");
        } else {
            tv_title.setText("编辑");
        }
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.tv_commit)
    public void onBackSave() {
        String content = ViewHelper.getText(et_feeder);
        if (StringUtils.isTrimEmpty(content)) {
            Toaster.showShort("");
            return;
        }
        mPresenter.commitOpinion(content);
    }

    @Override
    public void onCommitSuccess() {
        onBackPressed();
    }
}
