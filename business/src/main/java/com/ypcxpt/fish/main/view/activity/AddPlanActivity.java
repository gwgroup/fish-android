package com.ypcxpt.fish.main.view.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.StringUtils;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.contract.AddPlanContract;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.presenter.AddPlanPresenter;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

@Route(path = Path.Main.ADD_PLAN)
public class AddPlanActivity extends BaseActivity implements AddPlanContract.View {
    @BindView(R.id.tv_title) TextView tv_title;

    @BindView(R.id.tv_day) TextView tv_day;
    @BindView(R.id.tv_week) TextView tv_week;
    @BindView(R.id.tv_month) TextView tv_month;

    @BindView(R.id.rl_select_wm) RelativeLayout rl_select_wm;
    @BindView(R.id.tv_wm) TextView tv_wm;
    @BindView(R.id.tv_timeParent) TextView tv_timeParent;
    @BindView(R.id.tv_time) TextView tv_time;

    @BindView(R.id.et_feeder) EditText et_feeder;

    private AddPlanContract.Presenter mPresenter;

    @Autowired(name = "PLAN_TYPE")
    public int PLAN_TYPE;
    @Autowired(name = "IO_PLAN")
    public IoPlan ioPlan;

    @Override
    protected int layoutResID() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initData() {
        mPresenter = new AddPlanPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        if (PLAN_TYPE == 1) {
            tv_title.setText("添加定时");
        } else {
            tv_title.setText("编辑");
        }
    }

    private void closeColor() {
        tv_day.setBackgroundResource(R.drawable.bg_cycle);
        tv_day.setTextColor(getResources().getColor(R.color.common_838486));
        tv_week.setBackgroundResource(R.drawable.bg_cycle);
        tv_week.setTextColor(getResources().getColor(R.color.common_838486));
        tv_month.setBackgroundResource(R.drawable.bg_cycle);
        tv_month.setTextColor(getResources().getColor(R.color.common_838486));

        rl_select_wm.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_day, R.id.tv_week, R.id.tv_month})
    public void onContinued(View view) {
        switch (view.getId()) {
            case R.id.tv_day:
                closeColor();
                tv_day.setBackgroundResource(R.drawable.bg_cycle01);
                tv_day.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_week:
                closeColor();
                tv_week.setBackgroundResource(R.drawable.bg_cycle01);
                tv_week.setTextColor(getResources().getColor(R.color.white));
                rl_select_wm.setVisibility(View.VISIBLE);
                tv_wm.setText("每周");
                break;
            case R.id.tv_month:
                closeColor();
                tv_month.setBackgroundResource(R.drawable.bg_cycle01);
                tv_month.setTextColor(getResources().getColor(R.color.white));
                rl_select_wm.setVisibility(View.VISIBLE);
                tv_wm.setText("每月");
                break;
        }
    }

    @OnClick({R.id.rl_select_wm, R.id.rl_select_time})
    public void onSelectTime(View view) {
        switch (view.getId()) {
            case R.id.rl_select_wm:
                if ("每周".equals(tv_wm.getText().toString())) {
                    OptionPicker picker = new OptionPicker(this, new String[]{
                            "周一", "周二", "周三","周四","周五","周六","周日"
                    });
                    picker.setCanceledOnTouchOutside(false);
                    picker.setDividerRatio(WheelView.DividerConfig.FILL);
                    picker.setSelectedIndex(1);
                    picker.setCycleDisable(true);
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            tv_timeParent.setText(item + "");
                        }
                    });
                    picker.show();
                } else {
                    NumberPicker picker = new NumberPicker(this);
                    picker.setItemWidth(200);
                    picker.setCycleDisable(false);
                    picker.setOffset(3);//偏移量
                    picker.setRange(1, 28, 1);//数字范围
                    picker.setSelectedItem(1);
                    picker.setLabel("号");
                    picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                        @Override
                        public void onNumberPicked(int index, Number item) {
                            tv_timeParent.setText(item.intValue() + "号");
                        }
                    });
                    picker.show();
                }
                break;
            case R.id.rl_select_time:
                TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
                picker.setUseWeight(false);
                picker.setCycleDisable(false);
                picker.setRangeStart(0, 0);//00:00
                picker.setRangeEnd(23, 59);//23:59
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
                picker.setSelectedItem(currentHour, currentMinute);
                picker.setTopLineVisible(false);
                picker.setTextPadding(ConvertUtils.toPx(this, 15));
                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        tv_time.setText(hour + ":" + minute);
                    }
                });
                picker.show();
                break;
        }
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.tv_commit)
    public void onBackSave() {
        String content = ViewHelper.getText(et_feeder);
        if (StringUtils.isTrimEmpty(content)) {
            Toaster.showShort("");
            return;
        }
        mPresenter.commitOpinion(content);
    }

    @Override
    public void onCommitSuccess() {
        onBackPressed();
    }
}
