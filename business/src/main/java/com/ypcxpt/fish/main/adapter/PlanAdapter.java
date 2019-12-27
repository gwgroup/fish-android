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
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;
import com.ypcxpt.fish.main.view.fragment.TimingPlanFragment;

public class PlanAdapter extends BaseQuickAdapter<IoPlan, BaseViewHolder> {
    private TimingPlanContract.Presenter mPresenter;
    private Activity activity;

    private int index = 0;

    public PlanAdapter(int layoutResId, TimingPlanContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public PlanAdapter(int layoutResId, TimingPlanContract.Presenter presenter, Activity activity) {
        super(layoutResId);
        mPresenter = presenter;
        this.activity = activity;
    }

    public void setIndex(int itemIndex) {
        index = itemIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, IoPlan item) {
        String per = StringHelper.nullToDefault(item.per, "");
        ImageView imageView = helper.getView(R.id.iv_icon);
        if ("day".equals(per)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_day)
                    .into(imageView);
            helper.setText(R.id.tv_dateType, "每天");
        } else if ("week".equals(per)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_week)
                    .into(imageView);
            helper.setText(R.id.tv_dateType, praseNetWeek(item.day_of_week));
        } else if ("month".equals(per)) {
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_month)
                    .into(imageView);
            helper.setText(R.id.tv_dateType, "每月" + item.day_of_month + "号");
        }

        helper.setText(R.id.tv_name, "开启" + item.io_name);

        ImageView imageView2 = helper.getView(R.id.iv_useType);
        ImageView imageView3 = helper.getView(R.id.iv_useDuration);
        if ("feeder".equals(item.io_type)) {
            helper.setText(R.id.tv_duration, "投喂" + item.weight + "克");
            Glide.with(activity)
                    .load(R.mipmap.icon_main_feeder)
                    .into(imageView2);
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_feeder)
                    .into(imageView3);
        } else {
            if ("lamp".equals(item.io_type)) {
                Glide.with(activity)
                        .load(R.mipmap.icon_main_led)
                        .into(imageView2);
            } else if ("pump".equals(item.io_type)) {
                Glide.with(activity)
                        .load(R.mipmap.icon_main_pump)
                        .into(imageView2);
            } else if ("aerator".equals(item.io_type)) {
                Glide.with(activity)
                        .load(R.mipmap.icon_main_aerator)
                        .into(imageView2);
            }
            helper.setText(R.id.tv_duration, TimeUtil.generateTime(item.duration));
            Glide.with(activity)
                    .load(R.mipmap.icon_plan_duration)
                    .into(imageView3);
        }

        if (item.hour < 12) {
            helper.setText(R.id.tv_time, "上午 "
                    + String.format("%02d", item.hour) + ":"
                    + String.format("%02d", item.minute) + ":"
                    + String.format("%02d", item.second));
        } else if (item.hour == 12) {
            helper.setText(R.id.tv_time, "下午 "
                    + String.format("%02d", item.hour) + ":"
                    + String.format("%02d", item.minute) + ":"
                    + String.format("%02d", item.second));
        } else {
            helper.setText(R.id.tv_time, "下午 "
                    + String.format("%02d", item.hour - 12) + ":"
                    + String.format("%02d", item.minute) + ":"
                    + String.format("%02d", item.second));
        }

        if (item.enabled) {
            helper.getView(R.id.tv_planEnable).setBackgroundResource(R.drawable.bg_common_blue);
            helper.setText(R.id.tv_planEnable, "禁用计划");
        } else {
            helper.getView(R.id.tv_planEnable).setBackgroundResource(R.drawable.bg_common_red);
            helper.setText(R.id.tv_planEnable, "启用计划");
        }

        helper.getView(R.id.tv_planEnable).setOnClickListener(v -> {
            if (item.enabled) {
                //调用禁用计划接口
                mPresenter.closePlan(TimingPlanFragment.mMacAddress, item.id);
            } else {
                //调用启用计划接口
                mPresenter.openPlan(TimingPlanFragment.mMacAddress, item.id);
            }
        });

        helper.getView(R.id.tv_planEdit).setOnClickListener(v -> {
            Router.build(Path.Main.ADD_PLAN)
                    .withInt("SCENE_SELECTED", MyDeviceFragment.sceneSelected)
                    .withInt("PLAN_TYPE", 2)
                    .withParcelable("IO_PLAN", item)
                    .withString("DEVICE_MAC", TimingPlanFragment.mMacAddress)
                    .navigation(activity);
        });
    }

    private String praseNetWeek(int dayOfWeek){
        String week = "";
        if (dayOfWeek == 1) {
            week = "每周一";
        } else if (dayOfWeek == 2) {
            week = "每周二";
        } else if (dayOfWeek == 3) {
            week = "每周三";
        } else if (dayOfWeek == 4) {
            week = "每周四";
        } else if (dayOfWeek == 5) {
            week = "每周五";
        } else if (dayOfWeek == 6) {
            week = "每周六";
        } else if (dayOfWeek == 0) {
            week = "每周日";
        }
        return week;
    }
}