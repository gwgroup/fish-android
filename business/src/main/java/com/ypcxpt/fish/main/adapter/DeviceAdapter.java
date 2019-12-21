package com.ypcxpt.fish.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.main.contract.DeviceManagerContract;

public class DeviceAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private DeviceManagerContract.Presenter mPresenter;

    public DeviceAdapter(int layoutResId, DeviceManagerContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
//        String type = StringHelper.nullToDefault(item.type, "N/A");
//        String name = StringHelper.nullToDefault(item.name, "");
//        String macAddress = StringHelper.nullToDefault(item.macAddress, "");
//
//        helper.setText(R.id.tv_macAddress, "按摩椅编号：" + macAddress.replace(":", ""));
//        helper.setText(R.id.tv_type, type);
//        if (type.equals(name) || StringUtils.isTrimEmpty(name)) {
//            helper.getView(R.id.tv_name).setVisibility(View.GONE);
//        } else {
//            helper.getView(R.id.tv_name).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_name, "(" + name + ")");
//        }
//        helper.getView(R.id.rl_item).setOnClickListener(v -> {
////            mPresenter.skipDetail(item);
//        });
    }
}
