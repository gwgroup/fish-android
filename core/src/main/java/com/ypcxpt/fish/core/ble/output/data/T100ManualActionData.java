package com.ypcxpt.fish.core.ble.output.data;

import com.ypcxpt.fish.core.model.DeviceAction;

import java.util.List;

/**
 * T100相关指令数据.
 */
public class T100ManualActionData extends BaseActionData {
    private static List<DeviceAction> mPowerActionList;
    private final static int[] POWER_CODES = {0, 1};
    private final static String[] POWER_NAMES = {"关机", "开机"};

    private static List<DeviceAction> mNeckActionList;
    private final static int[] NECK_CODES = {2, 3, 4};
    private final static String[] NECK_NAMES = {"颈部按摩正", "颈部按摩反", "颈部按摩关"};

    private static List<DeviceAction> mNeck2ActionList;
    private final static int[] NECK2_CODES = {5, 6, 7};
    private final static String[] NECK2_NAMES = {"颈部按摩上", "颈部按摩下", "颈部按摩关"};

    private static List<DeviceAction> mBackActionList;
    private final static int[] BACK_CODES = {8, 9, 10, 11, 12};
    private final static String[] BACK_NAMES = {"全部按摩", "上背按摩", "下背按摩", "背部按摩关", "背部定点按摩"};

    private static List<DeviceAction> mAutoActionList;
    private final static int[] AUTO_CODES = {13, 14, 15, 16};
    private final static String[] AUTO_NAMES = {"自动模式1", "自动模式2", "自动模式3", "自动模式4"};

    private static List<DeviceAction> mAirActionList;
    private final static int[] AIR_CODES = {17, 18, 19, 20};
    private final static String[] AIR_NAMES = {"高", "中", "低", "关"};

    private static List<DeviceAction> mHeatActionList;
    private final static int[] HEAT_CODES = {21, 22};
    private final static String[] HEAT_NAMES = {"加热", "不加热"};

    public static List<DeviceAction> getPower() {
        if (mPowerActionList == null) {
            mPowerActionList = getDeviceActionList(POWER_CODES, POWER_NAMES);
        }
        return mPowerActionList;
    }

    public static List<DeviceAction> getNeck() {
        if (mNeckActionList == null) {
            mNeckActionList = getDeviceActionList(NECK_CODES, NECK_NAMES);
        }
        return mNeckActionList;
    }

    public static List<DeviceAction> getNeck2() {
        if (mNeck2ActionList == null) {
            mNeck2ActionList = getDeviceActionList(NECK2_CODES, NECK2_NAMES);
        }
        return mNeck2ActionList;
    }

    public static List<DeviceAction> getBack() {
        if (mBackActionList == null) {
            mBackActionList = getDeviceActionList(BACK_CODES, BACK_NAMES);
        }
        return mBackActionList;
    }

    public static List<DeviceAction> getAuto() {
        if (mAutoActionList == null) {
            mAutoActionList = getDeviceActionList(AUTO_CODES, AUTO_NAMES);
        }
        return mAutoActionList;
    }

    public static List<DeviceAction> getAir() {
        if (mAirActionList == null) {
            mAirActionList = getDeviceActionList(AIR_CODES, AIR_NAMES);
        }
        return mAirActionList;
    }

    public static List<DeviceAction> getHeat() {
        if (mHeatActionList == null) {
            mHeatActionList = getDeviceActionList(HEAT_CODES, HEAT_NAMES);
        }
        return mHeatActionList;
    }
}
