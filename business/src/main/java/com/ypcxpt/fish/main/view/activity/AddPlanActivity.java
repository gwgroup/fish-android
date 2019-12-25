package com.ypcxpt.fish.main.view.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.model.PlanParam;
import com.ypcxpt.fish.main.presenter.AddPlanPresenter;
import com.ypcxpt.fish.main.util.PlanDeleteDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

import static com.ypcxpt.fish.app.util.TimeUtil.generateTime;

@Route(path = Path.Main.ADD_PLAN)
public class AddPlanActivity extends BaseActivity implements AddPlanContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rl_add) RelativeLayout rl_add;

    @BindView(R.id.tv_day) TextView tv_day;
    @BindView(R.id.tv_week) TextView tv_week;
    @BindView(R.id.tv_month) TextView tv_month;

    @BindView(R.id.rl_select_wm) RelativeLayout rl_select_wm;
    @BindView(R.id.tv_wm) TextView tv_wm;
    @BindView(R.id.tv_timeParent) TextView tv_timeParent;
    @BindView(R.id.tv_time) TextView tv_time;

    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.rl_feeder) RelativeLayout rl_feeder;
    @BindView(R.id.et_feeder) EditText et_feeder;
    @BindView(R.id.rl_duration) RelativeLayout rl_duration;
    @BindView(R.id.tv_duration) TextView tv_duration;

    private AddPlanContract.Presenter mPresenter;

    @Autowired(name = "PLAN_TYPE")
    public int PLAN_TYPE;
    @Autowired(name = "DEVICE_MAC")
    public String DEVICE_MAC;
    @Autowired(name = "IO_PLAN")
    public IoPlan ioPlan;

    private List<IoInfo> ioInfoCurrents;

    private String id;
    private String per = "day";
    private int day_of_month = 0;
    private int day_of_week = -1;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private int duration = 0;
    private double weight = 0;

    private String io_code = "";
    private boolean enabled = true;

    @Override
    protected int layoutResID() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initData() {
        mPresenter = new AddPlanPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("DEVICE_MAC", DEVICE_MAC);
    }

    @Override
    protected void initViews() {
        if (PLAN_TYPE == 1) {
            tv_title.setText("添加定时");
            rl_add.setVisibility(View.GONE);
        } else {
            tv_title.setText("编辑定时");
            rl_add.setVisibility(View.VISIBLE);
            setEditPlanData();
        }
    }

    private void setEditPlanData() {
        id = ioPlan.id;
        per = ioPlan.per;
        day_of_month = ioPlan.day_of_month;
        day_of_week = ioPlan.day_of_week;
        hour = ioPlan.hour;
        minute = ioPlan.minute;
        second = ioPlan.second;
        duration = ioPlan.duration;
        weight = ioPlan.weight;
        io_code = ioPlan.io_code;
        enabled = ioPlan.enabled;

        if ("day".equals(ioPlan.per)) {
            closeColor();
            tv_day.setBackgroundResource(R.drawable.bg_cycle01);
            tv_day.setTextColor(getResources().getColor(R.color.white));
        } else if ("week".equals(ioPlan.per)) {
            closeColor();
            tv_week.setBackgroundResource(R.drawable.bg_cycle01);
            tv_week.setTextColor(getResources().getColor(R.color.white));
            rl_select_wm.setVisibility(View.VISIBLE);
            tv_wm.setText("每周");
            tv_timeParent.setText(praseNetWeek(ioPlan.day_of_week));
        } else if ("month".equals(ioPlan.per)) {
            closeColor();
            tv_month.setBackgroundResource(R.drawable.bg_cycle01);
            tv_month.setTextColor(getResources().getColor(R.color.white));
            rl_select_wm.setVisibility(View.VISIBLE);
            tv_wm.setText("每月");
            tv_timeParent.setText(ioPlan.day_of_month + "号");
        }

        tv_time.setText(DateUtils.fillZero(ioPlan.hour) + ":" + DateUtils.fillZero(ioPlan.minute));
        tv_name.setText(ioPlan.io_name);
        if ("feeder".equals(ioPlan.io_type)) {
            rl_feeder.setVisibility(View.VISIBLE);
            rl_duration.setVisibility(View.GONE);
            et_feeder.setText(ioPlan.weight + "");
        } else {
            rl_feeder.setVisibility(View.GONE);
            rl_duration.setVisibility(View.VISIBLE);
            tv_duration.setText(generateTime(ioPlan.duration));
        }
    }

    private String praseNetWeek(int dayOfWeek){
        String week = "";
        if (dayOfWeek == 1) {
            week = "周一";
        } else if (dayOfWeek == 2) {
            week = "周二";
        } else if (dayOfWeek == 3) {
            week = "周三";
        } else if (dayOfWeek == 4) {
            week = "周四";
        } else if (dayOfWeek == 5) {
            week = "周五";
        } else if (dayOfWeek == 6) {
            week = "周六";
        } else if (dayOfWeek == 0) {
            week = "周日";
        }
        return week;
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
                per = "day";
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
                tv_timeParent.setText("周一");

                per = "week";
                day_of_week = 1;
                break;
            case R.id.tv_month:
                closeColor();
                tv_month.setBackgroundResource(R.drawable.bg_cycle01);
                tv_month.setTextColor(getResources().getColor(R.color.white));
                rl_select_wm.setVisibility(View.VISIBLE);
                tv_wm.setText("每月");
                tv_timeParent.setText("1号");

                per = "month";
                day_of_month = 1;
                break;
        }
    }

    @OnClick({R.id.rl_select_wm, R.id.rl_select_time})
    public void onSelectTime(View view) {
        switch (view.getId()) {
            case R.id.rl_select_wm:
                if ("每周".equals(tv_wm.getText().toString())) {
                    OptionPicker picker = new OptionPicker(this, new String[]{
                            "周一", "周二", "周三", "周四", "周五", "周六", "周日"
                    });
                    picker.setCanceledOnTouchOutside(false);
                    picker.setDividerRatio(WheelView.DividerConfig.FILL);
                    picker.setSelectedIndex(0);
                    picker.setCycleDisable(true);
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            tv_timeParent.setText(item + "");

                            if ("周一".equals(item)) {
                                day_of_week = 1;
                            } else if ("周二".equals(item)) {
                                day_of_week = 2;
                            } else if ("周三".equals(item)) {
                                day_of_week = 3;
                            } else if ("周四".equals(item)) {
                                day_of_week = 4;
                            } else if ("周五".equals(item)) {
                                day_of_week = 5;
                            } else if ("周六".equals(item)) {
                                day_of_week = 6;
                            } else if ("周日".equals(item)) {
                                day_of_week = 0;
                            }

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

                            day_of_month = item.intValue();
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
                    public void onTimePicked(String hour2, String minute2) {
                        tv_time.setText(hour2 + ":" + minute2);

                        hour = Integer.parseInt(hour2);
                        minute = Integer.parseInt(minute2);
                    }
                });
                picker.show();
                break;
        }
    }

    @OnClick(R.id.rl_select_io)
    public void onSelectIO() {
        SinglePicker<IoInfo> picker = new SinglePicker<>(this, ioInfoCurrents);
        picker.setCanceledOnTouchOutside(false);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setOnItemPickListener((index, item) -> {
            io_code = item.code;
            tv_name.setText(item.name);

            if ("feeder".equals(item.type)) {
                rl_feeder.setVisibility(View.VISIBLE);
                rl_duration.setVisibility(View.GONE);
            } else {
                rl_feeder.setVisibility(View.GONE);
                rl_duration.setVisibility(View.VISIBLE);
            }
        });
        picker.show();
    }

    @OnClick(R.id.rl_duration)
    public void onSelectDuration() {
        //联动选择器的更多用法，可参见AddressPicker和CarNumberPicker
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return false;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i = 0; i <= 24; i++) {
                    String str = DateUtils.fillZero(i);
                    firstList.add(str);
                }
                return firstList;
            }

            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i = 0; i < 60; i++) {
                    String str = DateUtils.fillZero(i);
                    secondList.add(str);
                }
                return secondList;
            }

            @Nullable
            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                ArrayList<String> thirdList = new ArrayList<>();
                for (int i = 0; i < 60; i++) {
                    String str = DateUtils.fillZero(i);
                    thirdList.add(str);
                }
                return thirdList;
            }

        };
        LinkagePicker picker = new LinkagePicker(this, provider);
        picker.setCycleDisable(true);
        picker.setUseWeight(true);
        picker.setLabel("小时", "分钟", "秒");
        picker.setSelectedIndex(0, 1, 0);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                tv_duration.setText(first + "小时" + second + "分钟" + third + "秒");

                duration = (Integer.parseInt(first) * 60 * 60
                        + Integer.parseInt(second) * 60
                        + Integer.parseInt(third)) * 1000;
            }
        });
        picker.show();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.tv_commit)
    public void onBackSave() {
        if (io_code.contains("feeder")) {
            String content = ViewHelper.getText(et_feeder);
            if (!StringUtils.isEmpty(content)) {
                weight = Double.valueOf(content).doubleValue();
            } else {
                Toaster.showShort("您还没有输入投喂量");
            }
        }

        PlanParam planParam = new PlanParam();
        if (PLAN_TYPE == 1) {
            //添加计划
            planParam.setId(null);
            planParam.setPer(per);
            planParam.setDay_of_month(day_of_month);
            planParam.setDay_of_week(day_of_week);
            planParam.setHour(hour);
            planParam.setMinute(minute);
            planParam.setSecond(second);
            planParam.setIo_code(io_code);
            planParam.setWeight(weight);
            planParam.setDuration(duration);
            planParam.setEnabled(enabled);

            mPresenter.addPlan(DEVICE_MAC, planParam);
        } else {
            //编辑计划
            planParam.setId(id);
            planParam.setPer(per);
            planParam.setDay_of_month(day_of_month);
            planParam.setDay_of_week(day_of_week);
            planParam.setHour(hour);
            planParam.setMinute(minute);
            planParam.setSecond(second);
            planParam.setIo_code(io_code);
            planParam.setWeight(weight);
            planParam.setDuration(duration);
            planParam.setEnabled(enabled);

            mPresenter.editPlan(DEVICE_MAC, planParam);
        }
    }

    @Override
    public void showIoInfos(List<IoInfo> ioInfos) {
        for (int i = 0; i < ioInfos.size(); i++) {
            if (!ioInfos.get(i).enabled) {
                ioInfos.remove(ioInfos.get(i));
            }
        }
        ioInfoCurrents = ioInfos;

        if (ioInfoCurrents.size() > 0) {
            if (PLAN_TYPE == 1) {
                io_code = ioInfoCurrents.get(0).code;
                tv_name.setText(ioInfoCurrents.get(0).name);
                if ("feeder".equals(ioInfoCurrents.get(0).type)) {
                    rl_feeder.setVisibility(View.VISIBLE);
                    rl_duration.setVisibility(View.GONE);
                } else {
                    rl_feeder.setVisibility(View.GONE);
                    rl_duration.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onCommitSuccess() {
        onBackPressed();
    }

    @OnClick(R.id.rl_add)
    public void onDeletePlan() {
        PlanDeleteDialog planDeleteDialog = new PlanDeleteDialog(this, R.style.MyDialog);
        planDeleteDialog.setCancelable(false);
        planDeleteDialog.setOnResultListener(new PlanDeleteDialog.OnResultListener() {
            @Override
            public void Ok() {
                planDeleteDialog.dismiss();

                mPresenter.deletePlan(DEVICE_MAC, id);
            }

            @Override
            public void Cancel() {
                planDeleteDialog.dismiss();
            }
        });
        planDeleteDialog.show();
    }
}
