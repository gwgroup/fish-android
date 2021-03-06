package com.ypcxpt.fish.core.model;

import com.blankj.utilcode.util.StringUtils;

import java.util.List;

import static com.ypcxpt.fish.core.ble.output.data.BaseActionData.TYPE_INDIVIDUAL;
import static com.ypcxpt.fish.core.ble.output.data.BaseActionData.TYPE_LONG_CLICK_INDIVIDUAL;

public class ActionModel {
    public int type;

    private String bigName;

    public DeviceAction deviceAction;

    public List<DeviceAction> deviceActionList;

    public ActionModel(int type, DeviceAction deviceAction) {
        this.type = type;
        this.deviceAction = deviceAction;
    }

    public ActionModel(int type, String bigName, List<DeviceAction> deviceActionList) {
        this.type = type;
        this.bigName = bigName;
        this.deviceActionList = deviceActionList;
    }

    public boolean isIndividual() {
        return type == TYPE_INDIVIDUAL || type == TYPE_LONG_CLICK_INDIVIDUAL;
    }

    public String getBigName() {
        return StringUtils.isTrimEmpty(bigName) ? deviceAction.name : bigName;
    }

    @Override
    public String toString() {
        return "ManualActionModel{" +
                "type=" + type +
                ", bigName='" + bigName + '\'' +
                ", deviceAction=" + deviceAction +
                ", deviceActionList=" + deviceActionList +
                '}';
    }

}
