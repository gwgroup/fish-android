package com.ypcxpt.fish.core.ble.output.data;

import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.core.model.ManualActionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 手动模式相关数据.
 */
public class ManualActionData extends BaseActionData {
    private static List<DeviceAction> sBodyPartList;
    private static List<DeviceAction> sTechniqueModeList;
    private static List<DeviceAction> sAirIntensityList;
    private static List<DeviceAction> s3DStrengthList;
    private static List<DeviceAction> sWidthList;
    private static List<DeviceAction> sZeroGravityList;
    private static List<DeviceAction> sIndividualList;
    private static List<DeviceAction> sLongClickIndividualList;
    private static DeviceAction sPowerAction;
    private static DeviceAction sPauseAction;

    private static List<DeviceAction> sPressurePartList;

    /**
     * 开机指令.
     */
    public static DeviceAction getPowerAction() {
        if (sPowerAction == null) {
            sPowerAction = new DeviceAction(1, "开机");
        }
        return sPowerAction;
    }

    /**
     * 暂停指令.
     */
    public static DeviceAction getPauseAction() {
        if (sPauseAction == null) {
            sPauseAction = new DeviceAction(2, "暂停");
        }
        return sPauseAction;
    }

    /**
     * 手法.
     * "揉敲"等于"揉捏"+ "敲打".
     */
    private final static int[] TECHNIQUE_MODE_CODES = {28, 29, 30, 32, 31};
    private final static String[] TECHNIQUE_MODE_NAMES = {"揉捏", "敲打", "揉敲", "指压", "推拿"};

    /**
     * 幅度.
     */
    private final static int[] WIDTH_CODES = {33, 34, 35};
    private final static String[] WIDTH_NAMES = {"窄", "中", "宽"};

    /**
     * 部位切换.
     */
    private final static int[] BODY_PART_CODES = {36, 71, 72, 73, 74, 75};
    private final static String[] BODY_PART_NAMES = {"区域全身", "区间背部", "区间腰部", "区间座部", "区间背腰", "区间腰座"};

    /**
     * 气压部位.
     */
    private final static int[] STRENGTH_PRESSURE_CODES = {39, 40, 41, 42};
    private final static String[] STRENGTH_PRESSURE_NAMES = {"气压肩腰", "气压臀部", "气压手部", "气压腿部"};

    /**
     * 充气强度.
     */
    private final static int[] AIR_INTENSITY_CODES = {61, 62, 63, 64, 65};
    private final static String[] AIR_INTENSITY_NAMES = {"充气强度一档", "充气强度二档", "充气强度三档", "充气强度四档", "充气强度五档"};

    /**
     * 3D力度.
     */
    private final static int[] STRENGTH_3D_CODES = {84, 85, 86, 87, 88};
    private final static String[] STRENGTH_3D_NAMES = {"3D力度一档", "3D力度二档", "3D力度三档", "3D力度四档", "3D力度五档"};

    /**
     * 零重力.
     */
    private final static int[] ZERO_GRAVITY_CODES = {57, 58, 59};
    private final static String[] ZERO_GRAVITY_NAMES = {"零重力一档", "零重力二档", "零重力三档"};

    //零重力改为16试试
//    public static final int ZERO_GRAVITY_CODE = 59;
    public static final int ZERO_GRAVITY_CODE = 16;

    /**
     * 单项数据.
     */
//    private final static int[] INDIVIDUAL_CODES = {26, 25, 24};
    private final static int[] INDIVIDUAL_CODES = {24, 25, 26};//测试发现脚滚和腿滚反了所以这里改过来
    private final static String[] INDIVIDUAL_NAMES = {"脚滚", "热敷", "腿肚"};

    /**
     * 长按单项数据.
     */
    private final static int[] LONG_CLICK_INDIVIDUAL_CLICK_CODES = {12, 13, 17, 18, 14, 15};
    private final static String[] LONG_CLICK_INDIVIDUAL_CLICK_NAMES = {"升背", "降背", "腿部上", "腿部下", "腿部伸", "腿部缩"};

    public static List<DeviceAction> getWidthList() {
        if (sWidthList == null) {
            sWidthList = getDeviceActionList(WIDTH_CODES, WIDTH_NAMES);
        }
        return sWidthList;
    }

    public static List<DeviceAction> getPressurePartList() {
        if (sPressurePartList == null) {
            sPressurePartList = getDeviceActionList(STRENGTH_PRESSURE_CODES, STRENGTH_PRESSURE_NAMES);
        }
        return sPressurePartList;
    }

    public static List<DeviceAction> getTechniqueModeList() {
        if (sTechniqueModeList == null) {
            sTechniqueModeList = getDeviceActionList(TECHNIQUE_MODE_CODES, TECHNIQUE_MODE_NAMES);
        }
        return sTechniqueModeList;
    }

    public static List<DeviceAction> getBodyPartList() {
        if (sBodyPartList == null) {
            sBodyPartList = getDeviceActionList(BODY_PART_CODES, BODY_PART_NAMES);
        }
        return sBodyPartList;
    }

    public static List<DeviceAction> getAirIntensityList() {
        if (sAirIntensityList == null) {
            sAirIntensityList = getDeviceActionList(AIR_INTENSITY_CODES, AIR_INTENSITY_NAMES);
        }
        return sAirIntensityList;
    }

    public static List<DeviceAction> get3DStrengthList() {
        if (s3DStrengthList == null) {
            s3DStrengthList = getDeviceActionList(STRENGTH_3D_CODES, STRENGTH_3D_NAMES);
        }
        return s3DStrengthList;
    }

    public static List<DeviceAction> getZeroGravityList() {
        if (sZeroGravityList == null) {
            sZeroGravityList = getDeviceActionList(ZERO_GRAVITY_CODES, ZERO_GRAVITY_NAMES);
        }
        return sZeroGravityList;
    }

    public static List<DeviceAction> getIndividualList() {
        if (sIndividualList == null) {
            sIndividualList = getDeviceActionList(INDIVIDUAL_CODES, INDIVIDUAL_NAMES);
        }
        return sIndividualList;
    }

    public static List<DeviceAction> getLongClickIndividualList() {
        if (sLongClickIndividualList == null) {
            sLongClickIndividualList = getLongClickDeviceActionList(LONG_CLICK_INDIVIDUAL_CLICK_CODES, LONG_CLICK_INDIVIDUAL_CLICK_NAMES);
        }
        return sLongClickIndividualList;
    }

    public static List<ManualActionModel> getData() {
        List<ManualActionModel> data = new ArrayList<>();
        List<DeviceAction> individualList = getIndividualList();
        List<DeviceAction> longClickIndividualList = getLongClickIndividualList();

        data.add(new ManualActionModel(TYPE_INDIVIDUAL, individualList.get(0)));
        data.add(new ManualActionModel(TYPE_INDIVIDUAL, individualList.get(1)));
        data.add(new ManualActionModel(TYPE_TECHNIQUE, "手法", getTechniqueModeList()));
        data.add(new ManualActionModel(TYPE_BODY_PART, "部位切换", getBodyPartList()));
        data.add(new ManualActionModel(TYPE_AIR_INTENSITY, "充气强度", getAirIntensityList()));
        data.add(new ManualActionModel(TYPE_STRENGTH_3D, "3D力度", get3DStrengthList()));
        for (int i = 2; i < individualList.size(); i++) {
            data.add(new ManualActionModel(TYPE_INDIVIDUAL, individualList.get(i)));
        }
        for (int i = 0; i < longClickIndividualList.size(); i++) {
            data.add(new ManualActionModel(TYPE_LONG_CLICK_INDIVIDUAL, longClickIndividualList.get(i)));
        }

        return data;
    }

}
