package com.ypcxpt.fish.device.adapter;

import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.ble.output.data.AutoActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.library.util.ViewHelper;

public class AutoActionAdapter extends BaseQuickAdapter<DeviceAction, BaseViewHolder> {
    private BaseBLEPresenter mPresenter;

    public AutoActionAdapter(int layoutResId, BaseBLEPresenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
        setNewData(AutoActionData.get());
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceAction item) {
        TextView tvAction = helper.getView(R.id.tv_action);
        ViewHelper.setText(tvAction, item.name);
        tvAction.setOnClickListener(v -> {
            mPresenter.writeData(item);
            Log.e("**", item.name);
        });
    }

}
