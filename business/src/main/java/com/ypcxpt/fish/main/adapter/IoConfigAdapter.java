package com.ypcxpt.fish.main.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.IoConfigContract;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoInfoCurrent;
import com.ypcxpt.fish.main.util.CalibrationFeederDialog;
import com.ypcxpt.fish.main.util.FeederCheckDialog;
import com.ypcxpt.fish.main.util.PowerSetDialog;
import com.ypcxpt.fish.main.util.ScenesRenameDialog;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

import java.math.BigDecimal;

public class IoConfigAdapter extends BaseQuickAdapter<IoInfo, BaseViewHolder> {
    private IoConfigContract.Presenter mPresenter;
    private String mMac;

    public IoConfigAdapter(int layoutResId, String mac, IoConfigContract.Presenter presenter) {
        super(layoutResId);
        mMac = mac;
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
        helper.getView(R.id.sb_check).setClickable(false);
//        helper.setOnCheckedChangeListener(R.id.sb_check, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //启用
//                } else {
//                    //禁用
//                }
//            }
//        });

        helper.getView(R.id.tv_checkEnable).setOnClickListener(v -> {
            if (item.enabled) {
                //禁用
                mPresenter.disableIO(mMac, item.code);
            } else {
                mPresenter.enableIO(mMac, item.code);
            }
        });

        helper.getView(R.id.ll_rename).setOnClickListener(v -> {
            showRenameIODialog(mMac, item.code, item.name);
        });

        helper.getView(R.id.ll_setPower).setOnClickListener(v -> {
            showPowerIODialog(mMac, item.code, item.name);
        });

        helper.getView(R.id.ll_checkFeeder).setOnClickListener(v -> {
//            showCalibrationFeederDialog(mMac, item.code);
            showFeederCheck(mMac, item.code);
        });
    }

    private void showFeederCheck(String mMac, String code) {
        FeederCheckDialog feederCheckDialog = new FeederCheckDialog(mContext, R.style.MyDialog);
        feederCheckDialog.setCancelable(false);
        feederCheckDialog.setOnResultListener(new FeederCheckDialog.OnResultListener() {
            @Override
            public void Cancel() {
                feederCheckDialog.dismiss();

                showCalibrationFeederDialog(mMac, code);
            }
        });

        feederCheckDialog.show();
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

    private void showPowerIODialog(String mMac, String code, String name) {
        PowerSetDialog powerSetDialog = new PowerSetDialog(mContext, R.style.MyDialog);
        powerSetDialog.setIONameTitle(name);
        powerSetDialog.setCancelable(false);
        powerSetDialog.setOnResultListener(new PowerSetDialog.OnResultListener() {
            @Override
            public void Ok(String bark) {
                if (StringUtils.isTrimEmpty(bark)) {
                    Toaster.showShort("请输入功耗");
                } else {
                    mPresenter.setIOPower(mMac, code, Integer.parseInt(bark));
                    powerSetDialog.dismiss();
                }
            }

            @Override
            public void Cancel() {
                powerSetDialog.dismiss();
            }
        });
        powerSetDialog.show();
    }

    private void showRenameIODialog(String mMac, String code, String name) {
        ScenesRenameDialog scenesRenameDialog = new ScenesRenameDialog(mContext, name, R.style.MyDialog);
        scenesRenameDialog.setTitle("设备IO重命名");
        scenesRenameDialog.setCancelable(false);
        scenesRenameDialog.setOnResultListener(new ScenesRenameDialog.OnResultListener() {
            @Override
            public void Ok(String bark) {
                if (StringUtils.isTrimEmpty(bark)) {
                    Toaster.showShort("请输入名称");
                } else {
                    mPresenter.renameIO(mMac, code, bark);
                    scenesRenameDialog.dismiss();
                }
            }

            @Override
            public void Cancel() {
                scenesRenameDialog.dismiss();
            }
        });
        scenesRenameDialog.show();
    }
}
