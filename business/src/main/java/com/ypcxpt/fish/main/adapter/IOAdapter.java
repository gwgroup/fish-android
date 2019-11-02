package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.util.DurationSelectDialog;
import com.ypcxpt.fish.main.util.FeederDialog;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

public class IOAdapter extends BaseQuickAdapter<IoInfo, BaseViewHolder> {
    private MyDeviceContract.Presenter mPresenter;
    private Activity activity;

    private boolean isOpen = false;

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
//        helper.setChecked(R.id.sb_check, enable);

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

//        helper.setOnCheckedChangeListener(R.id.sb_check, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//                }
//            }
//        });
        helper.setText(R.id.tv_name, name);
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
            if (!isOpen) {
                if ("feeder".equals(item.type)) {
                    showFeederSelect(helper, item);
                } else {
                    showDurationSelect(helper, item);
                }
            } else {
                //关闭IO
                mPresenter.closeIO(MyDeviceFragment.macAddress, item.code);
                isOpen = false;
                helper.setChecked(R.id.sb_check, false);
            }

        });
    }

    private void showDurationSelect(BaseViewHolder helper, IoInfo item) {
        DurationSelectDialog selectDialog = new DurationSelectDialog(activity, R.style.MyDialog);
        selectDialog.setTitle(item.name);
        selectDialog.setCancelable(false);
        selectDialog.setOnResultListener(new DurationSelectDialog.OnResultListener() {

            @Override
            public void Ok(int hour, int minute, int second) {
                int duration = (hour * 60 * 60 + minute * 60 + second) * 1000;

                isOpen = true;
                mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
                helper.setChecked(R.id.sb_check, true);

                selectDialog.dismiss();
            }

            @Override
            public void Cancel() {
                selectDialog.dismiss();

                isOpen = false;
                helper.setChecked(R.id.sb_check, false);
            }
        });
        selectDialog.show();
    }

    private void showFeederSelect(BaseViewHolder helper, IoInfo item) {
        FeederDialog selectDialog = new FeederDialog(activity, R.style.MyDialog);
        selectDialog.setTitle(item.name);
        selectDialog.setCancelable(false);
        selectDialog.setOnResultListener(new FeederDialog.OnResultListener() {

            @Override
            public void Ok(int feeder) {
                int duration = (int) (feeder/item.weight_per_second * 1000);

                isOpen = true;
                mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
                helper.setChecked(R.id.sb_check, true);

                selectDialog.dismiss();
            }

            @Override
            public void Cancel() {
                selectDialog.dismiss();

                isOpen = false;
                helper.setChecked(R.id.sb_check, false);
            }

            @Override
            public void CalibrationFeeder() {
                selectDialog.dismiss();
            }
        });
        selectDialog.show();
    }
}