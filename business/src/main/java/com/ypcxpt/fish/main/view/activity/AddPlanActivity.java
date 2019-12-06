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
import com.ypcxpt.fish.main.presenter.AddPlanPresenter;

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

    private String per = "day";
    private int day_of_month = 0;
    private int day_of_week = -1;
    private String io_code = "";

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
        } else {
            tv_title.setText("编辑定时");
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
                    public void onTimePicked(String hour, String minute) {
                        tv_time.setText(hour + ":" + minute);
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
                for (int i = 1; i < 60; i++) {
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
        picker.setSelectedIndex(0, 0, 0);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                tv_duration.setText(first + "小时" + second + "分钟" + third + "秒");
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
        String content = ViewHelper.getText(et_feeder);
        if (StringUtils.isTrimEmpty(content)) {
            Toaster.showShort("");
            return;
        }
        mPresenter.commitOpinion(content);
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

    @Override
    public void onCommitSuccess() {
        onBackPressed();
    }
}
