package com.ypcxpt.fish.main.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.IoConfigContract;
import com.ypcxpt.fish.main.model.IoInfo;

public class IoConfigAdapter extends BaseQuickAdapter<IoInfo, BaseViewHolder> {
    private IoConfigContract.Presenter mPresenter;

    public IoConfigAdapter(int layoutResId, IoConfigContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, IoInfo item) {
        String name = StringHelper.nullToDefault(item.name, "");
        String type = StringHelper.nullToDefault(item.type, "");

        ImageView imageView = helper.getView(R.id.iv_icon);
        if (type.contains("lamp")) {
            Glide.with(mContext)
                    .load(R.mipmap.icon_main_led)
                    .into(imageView);
        } else if (type.contains("pump")) {
            Glide.with(mContext)
                    .load(R.mipmap.icon_main_pump)
                    .into(imageView);
        } else if (type.contains("aerator")) {
            Glide.with(mContext)
                    .load(R.mipmap.icon_main_aerator)
                    .into(imageView);
        } else if (type.contains("feeder")) {
            Glide.with(mContext)
                    .load(R.mipmap.icon_main_feeder)
                    .into(imageView);
        }

        if (type.contains("feeder")){
            helper.getView(R.id.ll_checkFeeder).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.ll_checkFeeder).setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_name, name);
        helper.setChecked(R.id.sb_check, item.enabled);
        helper.setOnCheckedChangeListener(R.id.sb_check, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //启用
                } else {
                    //禁用
                }
            }
        });

        helper.getView(R.id.rl_item).setOnClickListener(v -> {
        });
    }
}
