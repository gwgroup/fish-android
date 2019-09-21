package com.ypcxpt.fish.core.ble.output.data;

import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.core.model.DeviceAction;

import java.util.ArrayList;
import java.util.List;

public class BaseActionData {

    public static int TYPE_TECHNIQUE = 1;
    public static int TYPE_BODY_PART = 2;
    public static int TYPE_AIR_INTENSITY = 3;
    public static int TYPE_STRENGTH_3D = 4;
    public static int TYPE_INDIVIDUAL = 5;
    public static int TYPE_LONG_CLICK_INDIVIDUAL = 6;

    public static int ACTION_CODE_POWER = 1;

    public static List<DeviceAction> getDeviceActionList(int[] orderCodes, String[] actionNames) {
        return getClickDeviceActionList(orderCodes, actionNames, false);
    }

    public static List<DeviceAction> getLongClickDeviceActionList(int[] orderCodes, String[] actionNames) {
        return getClickDeviceActionList(orderCodes, actionNames, true);
    }

    private static List<DeviceAction> getClickDeviceActionList(int[] orderCodes, String[] actionNames, boolean longClick) {
        if (ObjectUtils.isEmpty(orderCodes) || ObjectUtils.isEmpty(actionNames)) {
            return null;
        }
        List<DeviceAction> actionList = new ArrayList<>();
        for (int i = 0; i < orderCodes.length; i++) {
            actionList.add(new DeviceAction(orderCodes[i], actionNames[i], longClick));
        }
        return actionList;
    }

}
