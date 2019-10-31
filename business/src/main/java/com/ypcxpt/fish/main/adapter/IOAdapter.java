package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.model.IoInfo;

public class IOAdapter extends BaseQuickAdapter<IoInfo, BaseViewHolder> {
    private MyDeviceContract.Presenter mPresenter;
    private Activity activity;

    private boolean enable;

    public IOAdapter(int layoutResId, MyDeviceContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public IOAdapter(int layoutResId, MyDeviceContract.Presenter presenter, Activity activity) {
        super(layoutResId);
        mPresenter = presenter;
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, IoInfo item) {
        String name = StringHelper.nullToDefault(item.name, "");
        String type = StringHelper.nullToDefault(item.type, "");
        enable = item.enabled;
        helper.setChecked(R.id.sb_check, enable);

        ImageView imageView = helper.getView(R.id.iv_icon);
        if (type.contains("lamp")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_led)
                    .into(imageView);
        } else if (type.contains("pump")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_pump)
                    .into(imageView);
        } else if (type.contains("aerator")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_aerator)
                    .into(imageView);
        } else if (type.contains("feeder")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_feeder)
                    .into(imageView);
        }
        helper.setText(R.id.tv_name, name);
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
            if (enable) {
                //这里执行关闭IO
                enable = false;
            } else {
                //这里执行打开IO
                enable = true;
            }
        });
    }
}