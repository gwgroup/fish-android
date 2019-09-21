package com.ypcxpt.fish.main.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.EditUserProfileContract;
import com.ypcxpt.fish.main.model.RegionInfo;

public class RegionAdapter extends BaseQuickAdapter<RegionInfo, BaseViewHolder> {
    private EditUserProfileContract.Presenter mPresenter;
    private Activity activity;

    public RegionAdapter(int layoutResId, EditUserProfileContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public RegionAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RegionInfo item) {
        String name = StringHelper.nullToDefault(item.getName(), "");
        helper.setText(R.id.tv_address, name);
    }
}
