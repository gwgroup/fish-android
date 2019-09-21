package com.ypcxpt.fish.device.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.device.adapter.AutoActionAdapter;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.view.activity.DeviceDetailActivity;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;

import butterknife.BindView;

public class AutoActionFragment extends BaseFragment {
    @BindView(R.id.rv) RecyclerView rv;

    private AutoActionAdapter mAdapter;

    @Override
    protected int layoutResID() {
        return R.layout.fragment_auto_mode;
    }

    @Override
    protected void initData() {
        BaseBLEPresenter blePresenter = ((DeviceDetailActivity) getActivity()).getBLEPresenter();
        mAdapter = new AutoActionAdapter(R.layout.item_auto_mode_action, blePresenter);
    }

    @Override
    protected void initViews() {
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(mAdapter);
    }
}
