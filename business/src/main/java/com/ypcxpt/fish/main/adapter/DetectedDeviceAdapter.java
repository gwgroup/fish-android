package com.ypcxpt.fish.main.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ypcxpt.fish.main.util.FloatMenu;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.definition.JConsumer;
import com.ypcxpt.fish.library.listener.RequestPermissionListener;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.PermissionHelper;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.view.activity.MainActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetectedDeviceAdapter extends BaseQuickAdapter<NetDevice, BaseViewHolder> {
    private MyDeviceContract.Presenter mPresenter;
    private Activity activity;

    public DetectedDeviceAdapter(int layoutResId, MyDeviceContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public DetectedDeviceAdapter(int layoutResId, MyDeviceContract.Presenter presenter, Activity activity) {
        super(layoutResId);
        mPresenter = presenter;
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, NetDevice item) {
        String type = StringHelper.nullToDefault(item.type, "N/A");
        String name = StringHelper.nullToDefault(item.name, "");
        String macAddress = StringHelper.nullToDefault(item.macAddress, "");
        //mac地址去冒号然后转化为long数字
//        long mac = Long.parseLong(macAddress.replace(":", ""), 16);
//        String macSub = "" + mac;
//        macSub = macSub.substring(macSub.length() - 6, macSub.length());

        helper.setText(R.id.tv_macAddress, "按摩椅编号：" + macAddress.replace(":", ""));
        helper.setText(R.id.tv_name, type);
        if (type.equals(name) || StringUtils.isTrimEmpty(name)) {
            helper.getView(R.id.tv_bake).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.tv_bake).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_bake, "(" + name + ")");
        }
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
//            BLEHelper.checkBluetoothState2(activity, () -> {
//                /* 蓝牙已开启 */
//                mPresenter.connectServices(item);
//            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /* 6.0起需要开启GPS权限，如果没允许，则不用继续下去 */
                requestGPSPermission(activity, success -> {
                    if (success) {
                        performCheckBluetoothState(activity, mPresenter, item);
                    } else {
                        Toaster.showLong("请允许开启GPS权限");
                    }
                });
            } else {
                performCheckBluetoothState(activity, mPresenter, item);
            }
        });
        helper.getView(R.id.rl_item).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final FloatMenu floatMenu = new FloatMenu(mContext, v);
                floatMenu.items(dip2px(mContext, 115),"备注设备", "解绑设备");
                floatMenu.show(MainActivity.point);
                floatMenu.setOnItemClickListener((v1, position) -> {
                    if (position == 0) {
//                            mPresenter.skipDetail(item);

                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View view = inflater.inflate(R.layout.dialog_layout_rename, null);
                        EditText et = view.findViewById(R.id.et_rename);
                        if (!StringUtils.isTrimEmpty(item.name)) {
                            et.setText(item.name);
                            et.setSelection(item.name.length());
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("备注设备名");
//                            final EditText et = new EditText(mContext);
//                            et.setHint("备注名");
//                            et.setSingleLine(true);
                        builder.setView(view);
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nameRemark = et.getText().toString();
                                NetDevice newNetDevice = buildNetDeviceRename(nameRemark, item.macAddress);
                                mPresenter.renameDevice(newNetDevice);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.main_color_new));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.main_color_new));
                    } else if (position == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("温馨提示");
                        builder.setMessage("确定要解绑该设备吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.removeDevice(item);
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.main_color_new));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.main_color_new));
                    }
                });

                return true;
            }
        });

        if (item.getScanResult() != null) {
            int rssi = item.getScanResult().getRssi();
//            Logger.e("********", "" + rssi + "," + macAddress);
//            if (rssi >= -60) {
////            holder.rssiStatus.setBackgroundResource(R.drawable.icon_green);
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#FF009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -65 && rssi < -60) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#FA009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -70 && rssi < -65) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#F5009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -75 && rssi < -70) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#F0009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -80 && rssi < -75) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#EB009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -82 && rssi < -80) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#D9009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -84 && rssi < -82) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#66009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -86 && rssi < -84) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#59009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -88 && rssi < -86) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#4D009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -88 && rssi < -86) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#40009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -90 && rssi < -88) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#33009900"), dpToPx(8), 0, 0);
//            } else if (rssi >= -100 && rssi < -90) {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#1A009900"), dpToPx(8), 0, 0);
//            } else {
//                ShadowDrawable.setShadowDrawable(helper.getView(R.id.rl_item), Color.parseColor("#FFFFFF"), dpToPx(10),
//                        Color.parseColor("#00009900"), dpToPx(8), 0, 0);
//            }
            if (rssi >= -82) {
                YoYo.with(Techniques.Shake)
                        .duration(1200)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(helper.getView(R.id.rl_item));
            }
        }

//        helper.getView(R.id.iv_remove).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.i("CCC", "点击移除解绑");
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
////                builder.setIcon(R.mipmap.ic_launcher_round);
//                builder.setTitle("温馨提示");
//                builder.setMessage("确定要解绑此设备吗？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mData.remove(item);
//                        Collections.sort(mData, BLE_SORTING_COMPARATOR);
//                        notifyDataSetChanged();
//                        mPresenter.removeDevice(item);
//                    }
//                });
//                builder.setNegativeButton("取消", null);
////                builder.setNeutralButton("中立", null);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
//        helper.getView(R.id.tv_rename).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.i("CCC", "点击重命名");
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("请重新命名");
//                final EditText et = new EditText(mContext);
//                et.setHint("设备名称");
//                et.setSingleLine(true);
//                builder.setView(et);
//                builder.setNegativeButton("取消", null);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String name = et.getText().toString();
//                        if (!StringUtils.isTrimEmpty(name)) {
//                            for (int i = 0; i < getData().size(); i++) {
//                                String traverseMacAddress = mData.get(i).macAddress;
//                                if (item.macAddress.equals(traverseMacAddress)) {
//                                    /* 同一个设备，刷新设备内容 */
//                                    mData.set(i, buildNetDeviceRename(name, item.macAddress));
//                                    notifyItemChanged(i);
//
//                                    NetDevice newNetDevice = buildNetDeviceRename(name, item.macAddress);
//                                    mPresenter.renameDevice(newNetDevice);
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 开启GPS.
     *
     * @param activity
     * @param callBack
     */
    private static void requestGPSPermission(Activity activity, JConsumer<Boolean> callBack) {
        new PermissionHelper().requestPermissions(activity, new RequestPermissionListener() {
            @Override
            public void onAcceptAllPermissions() {
                callBack.accept(true);
            }

            @Override
            public void onDenySomePermissions(List<String> deniedPermissions) {
                callBack.accept(false);
            }
        }, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION});
    }
    /**
     * 连接设备的时候开始检查蓝牙状态.
     *
     * @param activity
     * @param
     */
    private static void performCheckBluetoothState(Activity activity, MyDeviceContract.Presenter mPresenter, NetDevice device) {
        BluetoothAdapter adapter = ((BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if (adapter == null || !adapter.isEnabled()) {
            Toaster.showShort("请开启蓝牙，稍后重试");
        } else {
            mPresenter.connectServices(device);
        }
    }

    private int dpToPx(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp + 0.5f);
    }

    private NetDevice buildNetDeviceRename(String name, String mac) {
        NetDevice device = new NetDevice();
        device.macAddress = mac;
        device.name = name;
        return device;
    }

    public void addScanResult(ScanResult scanResult) {
        Logger.d("CCC", scanResult.toString());
        RxBleDevice newBLEDevice = scanResult.getBleDevice();
        String newMacAddress = newBLEDevice.getMacAddress();
        if (!StringUtils.isTrimEmpty(newBLEDevice.getName()) && (newBLEDevice.getName().trim().contains("HOME-8")
                || newBLEDevice.getName().trim().contains("HOME-6") || newBLEDevice.getName().trim().contains("T100"))) {
            /* 筛选出有名字的设备 */
            for (int i = 0; i < getData().size(); i++) {
                String traverseMacAddress = mData.get(i).macAddress;
                if (newMacAddress.equals(traverseMacAddress)) {
                    /* 同一个设备，刷新设备内容 */
                    mData.set(i, buildNetDevice(newBLEDevice,  mData.get(i).type, mData.get(i).name, scanResult));
                    notifyItemChanged(i);
                    return;
                }
            }

            /* 新设备，添加之 */
            NetDevice newNetDevice = buildNetDevice(newBLEDevice, scanResult);
            mData.add(newNetDevice);
            Collections.sort(mData, BLE_SORTING_COMPARATOR);
            notifyDataSetChanged();
            mPresenter.addDevice(newNetDevice);
        }

    }

    /* 蓝牙设备显示排序规则 */
    public static final Comparator<NetDevice> BLE_SORTING_COMPARATOR = (lhs, rhs) ->
            lhs.macAddress.compareTo(rhs.macAddress);

    /* 根据真实BLE设备构建临时设备Model */
    private NetDevice buildNetDevice(RxBleDevice bleDevice, ScanResult result) {
        NetDevice device = new NetDevice();
        device.macAddress = bleDevice.getMacAddress();
        if (bleDevice.getName().contains("_")) {
            device.type = bleDevice.getName().substring(0, bleDevice.getName().indexOf("_"));
        } else {
            device.type = bleDevice.getName();
        }
        device.setScanResult(result);
//        device.name = "HOME-8";
        return device;
    }

    /* 根据真实BLE设备构建临时设备Model */
    private NetDevice buildNetDevice(RxBleDevice bleDevice, String type, String name, ScanResult result) {
        NetDevice device = new NetDevice();
        device.macAddress = bleDevice.getMacAddress();
        device.type = type;
        device.name = name;
        device.setScanResult(result);
        return device;
    }

    public void addCodeScanResult(String type, String mac) {
        String newMacAddress = mac;
        if (!StringUtils.isTrimEmpty(type)) {
            /* 筛选出有名字的设备 */
            for (int i = 0; i < getData().size(); i++) {
                String traverseMacAddress = mData.get(i).macAddress;
                if (newMacAddress.equals(traverseMacAddress)) {
                    /* 同一个设备，刷新设备内容 */
                    mData.set(i, buildCodeNetDevice(mData.get(i).type, mData.get(i).name, mac));
                    notifyItemChanged(i);
                    return;
                }
            }

            /* 新设备，添加之 */
            NetDevice newNetDevice = buildCodeNetDevice(type, mac);
            mData.add(newNetDevice);
            Collections.sort(mData, BLE_SORTING_COMPARATOR);
            notifyDataSetChanged();
            mPresenter.addDevice(newNetDevice);
        }
    }

    /* 根据真实BLE设备构建临时设备Model */
    private NetDevice buildCodeNetDevice(String type, String mac) {
        NetDevice device = new NetDevice();
        device.macAddress = mac;
        device.type = type;
        return device;
    }

    private NetDevice buildCodeNetDevice(String type, String name, String mac) {
        NetDevice device = new NetDevice();
        device.macAddress = mac;
        device.type = type;
        device.name = name;
        return device;
    }

    public void clearScanResults() {
        mData.clear();
        notifyDataSetChanged();
    }
}
