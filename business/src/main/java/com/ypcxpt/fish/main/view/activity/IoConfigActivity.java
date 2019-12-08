package com.ypcxpt.fish.main.view.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.adapter.IoConfigAdapter;
import com.ypcxpt.fish.main.contract.IoConfigContract;
import com.ypcxpt.fish.main.event.OnRefreshUserEvent;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.presenter.IoConfigPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.IO_CONFIG)
public class IoConfigActivity extends BaseActivity implements IoConfigContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipe_refresh_layout;

    private IoConfigContract.Presenter mPresenter;
    private IoConfigAdapter mAdapter;

    @Autowired(name = "DEVICE_MAC")
    public String DEVICE_MAC;

    @Override
    protected int layoutResID() {
        return R.layout.activity_ioconfig;
    }

    @Override
    protected void initData() {
        mPresenter = new IoConfigPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("DEVICE_MAC", DEVICE_MAC);
    }

    @Override
    protected void initViews() {
        tv_title.setText("鱼塘配置");

        mAdapter = new IoConfigAdapter(R.layout.item_ioconfig, DEVICE_MAC, mPresenter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.bindToRecyclerView(rv);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(R.layout.include_d_m_nomsg);
        rv.setAdapter(mAdapter);
        mPresenter.acceptData("mAdapter", mAdapter);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        mPresenter.openDataFetching();
                        swipe_refresh_layout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new OnRefreshUserEvent());
        finish();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void showIoInfos(List<IoInfo> ioInfos) {
        mAdapter.setNewData(ioInfos);
    }
}
