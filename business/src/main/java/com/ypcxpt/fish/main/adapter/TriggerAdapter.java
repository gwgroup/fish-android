package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.TimingPlanContract;
import com.ypcxpt.fish.main.model.IoTrigger;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;
import com.ypcxpt.fish.main.view.fragment.TimingPlanFragment;

public class TriggerAdapter extends BaseQuickAdapter<IoTrigger, BaseViewHolder> {
    private TimingPlanContract.Presenter mPresenter;
    private Activity activity;

    private int index = 0;

    public TriggerAdapter(int layoutResId, TimingPlanContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public TriggerAdapter(int layoutResId, TimingPlanContract.Presenter presenter, Activity activity) {
        super(layoutResId);
        mPresenter = presenter;
        this.activity = activity;
    }

    public void setIndex(int itemIndex) {
        index = itemIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, IoTrigger item) {
        String monitor = StringHelper.nullToDefault(item.monitor, "");
        ImageView imageView = helper.getView(R.id.iv_icon);
        String triggerStr = "";
        if ("o2".equals(monitor)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_o2)
                    .into(imageView);
            triggerStr = "溶氧量";
            if ("<".equals(item.condition)) {
                triggerStr = triggerStr + "小于" + item.condition_val + "mg/L";
            } else {
                triggerStr = triggerStr + "大于" + item.condition_val + "mg/L";
            }
            helper.setText(R.id.tv_trigger, triggerStr);
        } else if ("ph".equals(monitor)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_ph)
                    .into(imageView);
            triggerStr = "PH值";
            if ("<".equals(item.condition)) {
                triggerStr = triggerStr + "小于" + item.condition_val;
            } else {
                triggerStr = triggerStr + "大于" + item.condition_val;
            }
            helper.setText(R.id.tv_trigger, triggerStr);
        } else if ("water_temperature".equals(monitor)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_temperature)
                    .into(imageView);
            triggerStr = "水温";
            if ("<".equals(item.condition)) {
                triggerStr = triggerStr + "低于" + item.condition_val + "℃";
            } else {
                triggerStr = triggerStr + "高于" + item.condition_val + "℃";
            }
            helper.setText(R.id.tv_trigger, triggerStr);
        }

        String operaction = StringHelper.nullToDefault(item.operaction, "");
        if ("open".equals(operaction)) {
            helper.setText(R.id.tv_control, "开启" + item.io_name);
        } else {
            helper.setText(R.id.tv_control, "关闭" + item.io_name);
        }

        helper.setText(R.id.tv_duration, "时长:" + TimeUtil.generateTime(item.duration));

        if (item.enabled) {
            helper.getView(R.id.tv_triggerEnable).setBackgroundResource(R.drawable.bg_common_blue);
            helper.setText(R.id.tv_triggerEnable, "禁用触发");
        } else {
            helper.getView(R.id.tv_triggerEnable).setBackgroundResource(R.drawable.bg_common_red);
            helper.setText(R.id.tv_triggerEnable, "启用触发");
        }

        helper.getView(R.id.tv_triggerEnable).setOnClickListener(v -> {
            if (item.enabled) {
                //调用禁用触发接口
                mPresenter.closeTrigger(TimingPlanFragment.mMacAddress, item.id);
            } else {
                //调用启用触发接口
                mPresenter.openTrigger(TimingPlanFragment.mMacAddress, item.id);
            }
        });

        helper.getView(R.id.tv_triggerEdit).setOnClickListener(v -> {
            Router.build(Path.Main.ADD_TRIGGER)
                    .withInt("SCENE_SELECTED", MyDeviceFragment.sceneSelected)
                    .withInt("TRIGGER_TYPE", 2)
                    .withParcelable("IO_TRIGGER", item)
                    .withString("DEVICE_MAC", TimingPlanFragment.mMacAddress)
                    .navigation(activity);
        });
    }
}