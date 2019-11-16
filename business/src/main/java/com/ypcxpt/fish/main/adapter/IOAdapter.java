package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.model.IoStatus;
import com.ypcxpt.fish.main.util.DurationSelectDialog;
import com.ypcxpt.fish.main.util.FeederDialog;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

public class IOAdapter extends BaseQuickAdapter<IoStatus, BaseViewHolder> {
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
    protected void convert(BaseViewHolder helper, IoStatus item) {
        String code = StringHelper.nullToDefault(item.code, "");

        helper.getView(R.id.sb_check).setClickable(false);
        helper.setChecked(R.id.sb_check, item.opened == 1);

        ImageView imageView = helper.getView(R.id.iv_icon);
        if (code.contains("lamp")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_led)
                    .into(imageView);
            helper.setText(R.id.tv_name, "灯");
        } else if (code.contains("pump")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_pump)
                    .into(imageView);
            helper.setText(R.id.tv_name, "水泵");
        } else if (code.contains("aerator")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_aerator)
                    .into(imageView);
            helper.setText(R.id.tv_name, "增氧机");
        } else if (code.contains("feeder")) {
            Glide.with(activity)
                    .load(R.mipmap.icon_main_feeder)
                    .into(imageView);
            helper.setText(R.id.tv_name, "投喂机");
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
                    showFeederSelect(item);
                } else {
                    showDurationSelect(item);
                }
            } else {
                //关闭IO
                mPresenter.closeIO(MyDeviceFragment.macAddress, item.code);
            }

        });
    }

    private void showDurationSelect(IoStatus item) {
        DurationSelectDialog selectDialog = new DurationSelectDialog(activity, R.style.MyDialog);
        selectDialog.setTitle(item.code);
        selectDialog.setCancelable(false);
        selectDialog.setOnResultListener(new DurationSelectDialog.OnResultListener() {

            @Override
            public void Ok(int hour, int minute, int second) {
                int duration = (hour * 60 * 60 + minute * 60 + second) * 1000;
                mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
                selectDialog.dismiss();
            }

            @Override
            public void Cancel() {
                selectDialog.dismiss();
            }
        });
        selectDialog.show();
    }

    private void showFeederSelect(IoStatus item) {
        FeederDialog selectDialog = new FeederDialog(activity, R.style.MyDialog);
        selectDialog.setTitle(item.code);
        selectDialog.setCancelable(false);
        selectDialog.setOnResultListener(new FeederDialog.OnResultListener() {

            @Override
            public void Ok(int feeder) {

//                if (item.weight_per_second > 0) {
                    int duration = (int) (feeder/25.4 * 1000);
                    mPresenter.openIO(MyDeviceFragment.macAddress, item.code, duration);
                    selectDialog.dismiss();
//                } else {
//                    Toaster.showShort("请校准投喂机");
//                }
            }

            @Override
            public void Cancel() {
                selectDialog.dismiss();
            }

            @Override
            public void CalibrationFeeder() {
                selectDialog.dismiss();
            }
        });
        selectDialog.show();
    }
}