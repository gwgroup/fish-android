package com.ypcxpt.fish.main.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.xw.repo.XEditText;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.DeviceManagerDetailContract;
import com.ypcxpt.fish.main.event.OnGetDevicesEvent;
import com.ypcxpt.fish.main.presenter.DeviceManagerDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.DEVICE_MANAGER_DETAIL)
public class DeviceManagerDetailActivity extends BaseActivity implements DeviceManagerDetailContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_device_name) XEditText et_device_name;
    @Autowired(name = "mDevice")
    public NetDevice mNetDevice;
    private DeviceManagerDetailContract.Presenter mPresenter;

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_manager_detail;
    }

    @Override
    protected void initData() {
        mPresenter = new DeviceManagerDetailPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("mDevice", mNetDevice);
    }

    @Override
    protected void initViews() {
        tv_title.setText(mNetDevice.type);
        et_device_name.setText(mNetDevice.name);
        ViewHelper.moveToLastCursor(et_device_name);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.rl_save)
    public void onBackSave() {
//        if (!StringUtils.isTrimEmpty(et_device_name.getText().toString())) {
            mPresenter.renameDevice(buildNetDeviceRename(et_device_name.getText().toString(), mNetDevice.macAddress));
//        } else {
//            Toaster.showShort("请输入设备名称");
//        }
    }

    @OnClick(R.id.rl_remove)
    public void onRemove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle("温馨提示");
        builder.setMessage("确定要解绑此设备吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.removeDevice(mNetDevice);
            }
        });
        builder.setNegativeButton("取消", null);
//                builder.setNeutralButton("中立", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onUpdateSuccess(UserProfile newProfile) {
//        EventBus.getDefault().post(new OnGetDeviceListEvent(newProfile.devices));
        EventBus.getDefault().post(new OnGetDevicesEvent());
        finish();
    }

    private NetDevice buildNetDeviceRename(String name, String mac) {
        NetDevice device = new NetDevice();
        device.macAddress = mac;
        device.name = name;
        return device;
    }
}
