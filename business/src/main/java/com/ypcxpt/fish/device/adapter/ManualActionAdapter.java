package com.ypcxpt.fish.device.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.ble.output.data.ManualActionData;
import com.ypcxpt.fish.core.model.ManualActionModel;
import com.ypcxpt.fish.device.event.OnManualActionItemClickEvent;
import com.ypcxpt.fish.library.util.ViewHelper;

import org.greenrobot.eventbus.EventBus;

public class ManualActionAdapter extends BaseQuickAdapter<ManualActionModel, BaseViewHolder> {

    public ManualActionAdapter(int layoutResId) {
        super(layoutResId);
        setNewData(ManualActionData.getData());
    }

    @Override
    protected void convert(BaseViewHolder helper, ManualActionModel item) {
        TextView tvAction = helper.getView(R.id.tv_action);
        ViewHelper.setText(tvAction, item.getBigName());
        tvAction.setOnClickListener(v -> {
            EventBus.getDefault().post(new OnManualActionItemClickEvent(item));
        });
    }
}
