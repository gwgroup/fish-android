package com.ypcxpt.fish.main.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.adapter.DeviceAdapter;
import com.ypcxpt.fish.main.contract.DeviceManagerContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.presenter.DeviceManagerPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.DEVICE_MANAGER)
public class DeviceManagerActivity extends BaseActivity implements DeviceManagerContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.ll_d_m_nodata) LinearLayout ll_d_m_nodata;

    private DeviceManagerContract.Presenter mPresenter;
    private DeviceAdapter mAdapter;

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_manager;
    }

    @Override
    protected void initData() {
        mPresenter = new DeviceManagerPresenter();
        addPresenter(mPresenter);
//        mPresenter.acceptData("mDevices", mDevices);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent(0)), 500);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initViews() {
        tv_title.setText("设备管理");
        mAdapter = new DeviceAdapter(R.layout.item_deivce_manager, mPresenter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.bindToRecyclerView(rv);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(R.layout.include_d_m_nodata);
        rv.setAdapter(mAdapter);
        mPresenter.acceptData("mAdapter", mAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    @Subscribe
    public void onEventReceived(OnGetScenesEvent event) {
        //收到通知调用接口获取设备列表
        mPresenter.getDevices();
    }

}
