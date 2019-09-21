package com.ypcxpt.fish.core.ble.output.data;

import com.ypcxpt.fish.core.model.DeviceAction;

import java.util.List;

/**
 * 手动模式相关数据.
 */
public class AutoActionData extends BaseActionData {
    private final static int[] ORDER_CODES = {3, 4, 5, 6, 7, 8, 9, 10, 69, 70};

    private final static String[] ACTION_NAMES = {"活力恢复", "肩颈护理", "腰部舒缓", "臀部塑型", "牵引拉伸", "智能呵护", "指尖唤醒", "商务休闲", "助眠修养", "缓解放松"};

    private static List<DeviceAction> mAutoActionList;

    public static List<DeviceAction> get() {
        if (mAutoActionList == null) {
            mAutoActionList = getDeviceActionList(ORDER_CODES, ACTION_NAMES);
        }
        return mAutoActionList;
    }

}
