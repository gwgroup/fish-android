package com.ypcxpt.fish.core.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ypcxpt.fish.library.definition.JAction;
import com.ypcxpt.fish.library.definition.JConsumer;
import com.ypcxpt.fish.library.listener.RequestPermissionListener;
import com.ypcxpt.fish.library.util.HexString;
import com.ypcxpt.fish.library.util.PermissionHelper;
import com.ypcxpt.fish.library.util.Toaster;

import java.util.List;

public class BLEHelper {

    /* REQUEST_CODE 申请开启蓝牙 */
    public static int TURN_ON_BLUETOOTH = 0x1000;

    /* 最大扫描时间 */
    public static final int MAX_SCAN_TIME = 3 * 1000;

    /* 写入数据相关 */
    public static final int WRITE_HEADER = 0x06;
    public static final int WRITE_ADDRESS = 0X02;
    public static final int WRITE_DATA_1 = 0x00;
    /* 写入贝壳椅数据相关 */
    public static final int WRITE_HEADER_SHELL = 0xAB;
    public static final int WRITE_ADDRESS_SHELL = 0x55;
    public static final int WRITE_DATA_TYPE_SHELL = 0x16;

    /**
     * 检测蓝牙状态.
     *
     * @param activity
     * @param hasTurnedOn 蓝牙已经开启.
     */
    public static void checkBluetoothState(Activity activity, JAction hasTurnedOn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /* 6.0起需要开启GPS权限，如果没允许，则不用继续下去 */
            requestGPSPermission(activity, success -> {
                if (success) {
                    performCheckBluetoothState(activity, hasTurnedOn);
                } else {
                    Toaster.showLong("请允许开启GPS权限");
                }
            });
        } else {
            performCheckBluetoothState(activity, hasTurnedOn);
        }
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
     * 开始检查蓝牙状态.
     *
     * @param activity
     * @param hasTurnedOn
     */
    private static void performCheckBluetoothState(Activity activity, JAction hasTurnedOn) {
        BluetoothAdapter adapter = ((BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if (adapter == null || !adapter.isEnabled()) {
            turnOnBluetooth(activity);
        } else {
            hasTurnedOn.run();
        }
    }

    /**
     * 开启蓝牙.
     *
     * @param activity
     */
    private static void turnOnBluetooth(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, TURN_ON_BLUETOOTH);
    }


    /**
     * HOME-8
     * eg:
     * 包头         地址      数据0     数据1     校验码
     * 0x06/0x03   0x02      0x__	  0X00     0X__
     * <p>
     * 共5段两位16进制数据.
     *
     * @param code 数据0
     * @return
     */
    public static String wrapInputData(int code) {
        int checkCode = WRITE_HEADER + WRITE_ADDRESS + code + WRITE_DATA_1;
        checkCode = checkCode & 0xFF;
        return new StringBuilder()
                .append(HexString.toHexString(WRITE_HEADER))
                .append(HexString.toHexString(WRITE_ADDRESS))
                .append(HexString.toHexString(code))
                .append(HexString.toHexString(WRITE_DATA_1))
                .append(HexString.toHexString(checkCode))
                .toString();
    }

    /**
     * 贝壳椅
     * eg:
     * 包头         地址      数据0     数据1     校验码
     * 0xAA/0xAB   0x55      0x16	  0X__     0X__
     * <p>
     * 共5段两位16进制数据.
     *
     * @param code 数据0
     * @return
     */
    public static String wrapInputDataWithShellchair(int code) {
        int checkCode = WRITE_HEADER_SHELL + WRITE_ADDRESS_SHELL + WRITE_DATA_TYPE_SHELL + code;
        checkCode = checkCode & 0xFF;
        return new StringBuilder()
                .append(HexString.toHexString(WRITE_HEADER_SHELL))
                .append(HexString.toHexString(WRITE_ADDRESS_SHELL))
                .append(HexString.toHexString(WRITE_DATA_TYPE_SHELL))
                .append(HexString.toHexString(code))
                .append(HexString.toHexString(checkCode))
                .toString();
    }

    /**
     * 计算完校验码之后返回数据给按摩椅
     * @param code
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static String sendNowStatusDataWithT100(int code, int year, int month, int day, int hour, int minute) {
        int checkCode = 0x23 + 0x75 + 0x07 + code + 0x00 + year + month + day + hour + minute;
        checkCode = checkCode & 0xFF;
        return new StringBuilder()
                .append(HexString.toHexString(0x23))
                .append(HexString.toHexString(0x75))
                .append(HexString.toHexString(0x07))
                .append(HexString.toHexString(code))
                .append(HexString.toHexString(0x00))
                .append(HexString.toHexString(year))
                .append(HexString.toHexString(month))
                .append(HexString.toHexString(day))
                .append(HexString.toHexString(hour))
                .append(HexString.toHexString(minute))
                .append(HexString.toHexString(checkCode))
                .toString();
    }

    public static String wrapInputDataWithT100(int code) {
        int checkCode = 0x23 + 0x75 + 0x03 + 0x01 + 0x01 + code;
        checkCode = checkCode & 0xFF;
        return new StringBuilder()
                .append(HexString.toHexString(0x23))
                .append(HexString.toHexString(0x75))
                .append(HexString.toHexString(0x03))
                .append(HexString.toHexString(0x01))
                .append(HexString.toHexString(0x01))
                .append(HexString.toHexString(code))
                .append(HexString.toHexString(checkCode))
                .toString();
    }
}
