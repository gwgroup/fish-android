package com.ypcxpt.fish.core.ble.output.data;

import com.ypcxpt.fish.core.model.DeviceAction;

import java.util.List;

/**
 * 贝壳椅自动模式相关数据.
 */
public class ShellAutoActionData extends BaseActionData {
    private final static int[] ORDER_CODES = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    private final static String[] ACTION_NAMES = {"全身放松", "舒压调节", "活力唤醒", "牵引拉伸", "轻度休息", "指尖唤醒", "颈肩按摩", "护理修心", "女性舒缓", "温热初始", "快速颈肩", "快速腰臀"};

    private static List<DeviceAction> mAutoActionList;

    public static List<DeviceAction> get() {
        if (mAutoActionList == null) {
            mAutoActionList = getDeviceActionList(ORDER_CODES, ACTION_NAMES);
        }
        return mAutoActionList;
    }

}
