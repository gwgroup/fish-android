package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.model.IoInfoCurrent;
import com.ypcxpt.fish.main.util.CalibrationFeederDialog;
import com.ypcxpt.fish.main.util.DurationSelectDialog;
import com.ypcxpt.fish.main.util.FeederCheckDialog;
import com.ypcxpt.fish.main.util.FeederDialog;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.util.DateUtils;

public class IOAdapter extends BaseQuickAdapter<IoInfoCurrent, BaseViewHolder> {
    private MyDeviceContract.Presenter mPresenter;
    private Activity activity;

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
    protected void convert(BaseViewHolder helper, IoInfoCurrent item) {
        String code = StringHelper.nullToDefault(item.code, "");
        String name = StringHelper.nullToDefault(item.name, "");

        helper.getView(R.id.sb_check).setClickable(false);
        helper.setChecked(R.id.sb_check, item.opened == 1);
        helper.setText(R.id.tv_name, name);
        ImageView imageView = helper.getView(R.id.iv_icon);
        if (code.contains("lamp")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_led)
                    .into(imageView);
        } else if (code.contains("pump")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_pump)
                    .into(imageView);
        } else if (code.contains("aerator")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_aerator)
                    .into(imageView);
        } else if (code.contains("feeder")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_feeder)
                    .into(imageView);
        }

//        helper.setOnCheckedChangeListener(R.id.sb_check, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//                }
//            }
//        });
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
            if (item.opened == 0) {
                if (code.contains("feeder")) {
                    if (SPHelper.getBoolean("FEEDER_CHECKED", false)) {
                        showFeederSelect(item);
                    } else {
                        //校准投喂机
                        showFeederCheck(item);
                    }
                } else {
                    showDurationSelect(item);
                }
            } else {
                //关闭IO
                mPresenter.closeIO(MyDeviceFragment.macAddress, item.code);
            }

        });
    }

    private void showDurationSelect(IoInfoCurrent item) {
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
        LinkagePicker picker = new LinkagePicker(activity, provider);
        picker.setCycleDisable(true);
        picker.setUseWeight(true);
        picker.setLabel("小时", "分钟", "秒");
        picker.setSelectedIndex(0, 1, 0);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                int duration = (Integer.parseInt(first) * 60 * 60
                        + Integer.parseInt(second) * 60
                        + Integer.parseInt(third)) * 1000;
                mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
            }
        });
        picker.show();
//        DurationSelectDialog selectDialog = new DurationSelectDialog(activity, R.style.MyDialog);
//        selectDialog.setTitle(item.code);
//        selectDialog.setCancelable(false);
//        selectDialog.setOnResultListener(new DurationSelectDialog.OnResultListener() {
//
//            @Override
//            public void Ok(int hour, int minute, int second) {
//                int duration = (hour * 60 * 60 + minute * 60 + second) * 1000;
//                mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
//                selectDialog.dismiss();
//            }
//
//            @Override
//            public void Cancel() {
//                selectDialog.dismiss();
//            }
//        });
//        selectDialog.show();
    }

    private void showFeederCheck(IoInfoCurrent item) {
        FeederCheckDialog feederCheckDialog = new FeederCheckDialog(activity, R.style.MyDialog);
        feederCheckDialog.setCancelable(false);
        feederCheckDialog.setOnResultListener(new FeederCheckDialog.OnResultListener() {
            @Override
            public void Cancel() {
                feederCheckDialog.dismiss();

                showCalibrationFeederDialog(MyDeviceFragment.macAddress, item.code);
            }

            @Override
            public void Feeder() {
                if (item.weight_per_second > 0) {
                    mPresenter.openIO(MyDeviceFragment.macAddress, item.code, 10000);
                }
            }
        });

        feederCheckDialog.show();
    }

    private void showFeederSelect(IoInfoCurrent item) {
        FeederDialog selectDialog = new FeederDialog(activity, R.style.MyDialog);
        selectDialog.setTitle(item.code);
        selectDialog.setCancelable(false);
        selectDialog.setOnResultListener(new FeederDialog.OnResultListener() {

            @Override
            public void Ok(int feeder) {

                if (item.weight_per_second > 0) {
                    int duration = (int) (feeder/item.weight_per_second * 1000);
                    mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
                    selectDialog.dismiss();
                } else {
                    Toaster.showShort("请校准投喂机");
                }
            }

            @Override
            public void Cancel() {
                selectDialog.dismiss();
            }

            @Override
            public void CalibrationFeeder() {
                //重新校准投喂机
                selectDialog.dismiss();

//                showCalibrationFeederDialog(MyDeviceFragment.macAddress, item.code);
                showFeederCheck(item);
            }
        });
        selectDialog.show();
    }

    private void showCalibrationFeederDialog(String mMac, String code) {
        CalibrationFeederDialog calibrationFeederDialog = new CalibrationFeederDialog(mContext, R.style.MyDialog);
        calibrationFeederDialog.setCancelable(false);
        calibrationFeederDialog.setOnResultListener(new CalibrationFeederDialog.OnResultListener() {
            @Override
            public void Ok(String bark) {
                if (StringUtils.isTrimEmpty(bark)) {
                    Toaster.showShort("请输入饲料重量");
                } else {
                    BigDecimal b = new BigDecimal(Double.valueOf(bark).doubleValue());
                    mPresenter.calibrationFeeder(mMac, code, b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
                    calibrationFeederDialog.dismiss();
                }
            }

            @Override
            public void Cancel() {
                calibrationFeederDialog.dismiss();
            }
        });
        calibrationFeederDialog.show();
    }
}