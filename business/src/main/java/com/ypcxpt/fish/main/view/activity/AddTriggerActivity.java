package com.ypcxpt.fish.main.view.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.contract.AddTriggerContract;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoTrigger;
import com.ypcxpt.fish.main.model.TriggerParam;
import com.ypcxpt.fish.main.presenter.AddTriggerPresenter;
import com.ypcxpt.fish.main.util.PlanDeleteDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

@Route(path = Path.Main.ADD_TRIGGER)
public class AddTriggerActivity extends BaseActivity implements AddTriggerContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rl_add) RelativeLayout rl_add;

    @BindView(R.id.tv_water) TextView tv_water;
    @BindView(R.id.tv_ph) TextView tv_ph;
    @BindView(R.id.tv_o2) TextView tv_o2;

    @BindView(R.id.tv_select) TextView tv_select;
    @BindView(R.id.tv_value) TextView tv_value;
    @BindView(R.id.tv_openclose) TextView tv_openclose;

    @BindView(R.id.tv_name) TextView tv_name;

    private AddTriggerContract.Presenter mPresenter;

    @Autowired(name = "TRIGGER_TYPE")
    public int TRIGGER_TYPE;
    @Autowired(name = "DEVICE_MAC")
    public String DEVICE_MAC;
    @Autowired(name = "IO_TRIGGER")
    public IoTrigger ioTrigger;

    private List<IoInfo> ioInfoCurrents;

    private String id;
    private String monitor = "water_temperature";
    private String condition = ">";
    private String operaction = "";
    private double condition_val = 0;
    private String io_code = "";
    private boolean enabled = true;

    DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected int layoutResID() {
        return R.layout.activity_add_trigger;
    }

    @Override
    protected void initData() {
        mPresenter = new AddTriggerPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("DEVICE_MAC", DEVICE_MAC);
    }

    @Override
    protected void initViews() {
        if (TRIGGER_TYPE == 1) {
            tv_title.setText("添加触发任务");
            rl_add.setVisibility(View.GONE);
        } else {
            tv_title.setText("编辑触发任务");
            rl_add.setVisibility(View.VISIBLE);
            setEditPlanData();
        }
    }

    private void setEditPlanData() {
        id = ioTrigger.id;
        monitor = ioTrigger.monitor;
        if ("大于".equals(ioTrigger.condition)) {
            condition = ">";
            tv_select.setText("大于");
        } else {
            condition = "<";
            tv_select.setText("小于");
        }
        condition_val = ioTrigger.condition_val;
        operaction = ioTrigger.operaction;
        io_code = ioTrigger.io_code;
        enabled = ioTrigger.enabled;

        if ("water_temperature".equals(ioTrigger.monitor)) {
            closeColor();
            tv_water.setBackgroundResource(R.drawable.bg_cycle01);
            tv_water.setTextColor(getResources().getColor(R.color.white));
            tv_value.setText(condition_val + "℃");
        } else if ("ph".equals(ioTrigger.monitor)) {
            closeColor();
            tv_ph.setBackgroundResource(R.drawable.bg_cycle01);
            tv_ph.setTextColor(getResources().getColor(R.color.white));
            tv_value.setText(condition_val + "");
        } else if ("o2".equals(ioTrigger.monitor)) {
            closeColor();
            tv_o2.setBackgroundResource(R.drawable.bg_cycle01);
            tv_o2.setTextColor(getResources().getColor(R.color.white));
            tv_value.setText(condition_val + "mg/L");
        }

        tv_name.setText(ioTrigger.io_name);
        if ("open".equals(ioTrigger.operaction)) {
            tv_openclose.setText("打开");
        } else {
            tv_openclose.setText("关闭");
        }
    }

    private void closeColor() {
        tv_water.setBackgroundResource(R.drawable.bg_cycle);
        tv_water.setTextColor(getResources().getColor(R.color.common_838486));
        tv_ph.setBackgroundResource(R.drawable.bg_cycle);
        tv_ph.setTextColor(getResources().getColor(R.color.common_838486));
        tv_o2.setBackgroundResource(R.drawable.bg_cycle);
        tv_o2.setTextColor(getResources().getColor(R.color.common_838486));
    }

    @OnClick({R.id.tv_water, R.id.tv_ph, R.id.tv_o2})
    public void onContinued(View view) {
        switch (view.getId()) {
            case R.id.tv_water:
                closeColor();
                tv_water.setBackgroundResource(R.drawable.bg_cycle01);
                tv_water.setTextColor(getResources().getColor(R.color.white));
                monitor = "water_temperature";
                break;
            case R.id.tv_ph:
                closeColor();
                tv_ph.setBackgroundResource(R.drawable.bg_cycle01);
                tv_ph.setTextColor(getResources().getColor(R.color.white));
                monitor = "ph";
                break;
            case R.id.tv_o2:
                closeColor();
                tv_o2.setBackgroundResource(R.drawable.bg_cycle01);
                tv_o2.setTextColor(getResources().getColor(R.color.white));
                monitor = "o2";
                break;
        }
    }

    @OnClick({R.id.rl_select, R.id.rl_select_value})
    public void onSelectValue(View view) {
        switch (view.getId()) {
            case R.id.rl_select:
                OptionPicker picker = new OptionPicker(this, new String[]{
                        "大于", "小于"
                });
                picker.setCanceledOnTouchOutside(false);
                picker.setDividerRatio(WheelView.DividerConfig.FILL);
                picker.setSelectedIndex(0);
                picker.setCycleDisable(true);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tv_select.setText(item + "");

                        if ("大于".equals(item)) {
                            condition = ">";
                        } else {
                            condition = "<";
                        }

                    }
                });
                picker.show();
                break;
            case R.id.rl_select_value:
                if ("water_temperature".equals(monitor)) {
                    NumberPicker picker2 = new NumberPicker(this);
                    picker2.setItemWidth(200);
                    picker2.setCycleDisable(false);
                    picker2.setOffset(3);//偏移量
                    picker2.setRange(-20, 40, 1);//数字范围
                    picker2.setSelectedItem(1);
                    picker2.setLabel("℃");
                    picker2.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                        @Override
                        public void onNumberPicked(int index, Number item) {
                            tv_value.setText(item.intValue() + "℃");

                            condition_val = item.intValue();
                        }
                    });
                    picker2.show();
                } else if ("ph".equals(monitor)) {
                    NumberPicker picker2 = new NumberPicker(this);
                    picker2.setItemWidth(200);
                    picker2.setCycleDisable(false);
                    picker2.setOffset(3);//偏移量
                    picker2.setRange(0, 14, 1);//数字范围
                    picker2.setSelectedItem(1);
                    picker2.setLabel("");
                    picker2.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                        @Override
                        public void onNumberPicked(int index, Number item) {
                            tv_value.setText(item.intValue() + "");

                            condition_val = item.intValue();
                        }
                    });
                    picker2.show();
                } else if ("o2".equals(monitor)) {
                    NumberPicker picker2 = new NumberPicker(this);
                    picker2.setItemWidth(200);
                    picker2.setCycleDisable(false);
                    picker2.setOffset(3);//偏移量
                    picker2.setRange(0, 30, 0.1);//数字范围
                    picker2.setSelectedItem(1);
                    picker2.setLabel("mg/L");
                    picker2.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                        @Override
                        public void onNumberPicked(int index, Number item) {
                            tv_value.setText(df.format(item.doubleValue()) + "");

                            BigDecimal b = new BigDecimal(item.doubleValue());
                            condition_val =  b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                    });
                    picker2.show();
                }
                break;
        }
    }

    @OnClick(R.id.rl_openclose)
    public void onOpenclose() {
        OptionPicker picker = new OptionPicker(this, new String[]{
                "打开", "关闭"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tv_openclose.setText(item + "");

                if ("打开".equals(item)) {
                    operaction = "open";
                } else {
                    operaction = "close";
                }

            }
        });
        picker.show();
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
        });
        picker.show();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.tv_commit)
    public void onBackSave() {
        TriggerParam triggerParam = new TriggerParam();
        if (TRIGGER_TYPE == 1) {
            //添加计划
            triggerParam.setId(null);
            triggerParam.setOperaction(operaction);
            triggerParam.setCondition(condition);
            triggerParam.setCondition_val(condition_val);
            triggerParam.setEnabled(enabled);
            triggerParam.setMonitor(monitor);
            triggerParam.setIo_code(io_code);

            mPresenter.addPlan(DEVICE_MAC, triggerParam);
        } else {
            //编辑计划
            triggerParam.setId(id);
            triggerParam.setId(null);
            triggerParam.setOperaction(operaction);
            triggerParam.setCondition(condition);
            triggerParam.setCondition_val(condition_val);
            triggerParam.setEnabled(enabled);
            triggerParam.setMonitor(monitor);
            triggerParam.setIo_code(io_code);

            mPresenter.editPlan(DEVICE_MAC, triggerParam);
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
            if (TRIGGER_TYPE == 1) {
                io_code = ioInfoCurrents.get(0).code;
                tv_name.setText(ioInfoCurrents.get(0).name);
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
