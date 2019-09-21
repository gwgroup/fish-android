package com.ypcxpt.fish.device.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.ble.input.DeviceDataParser;
import com.ypcxpt.fish.core.ble.output.data.DeviceConstant;
import com.ypcxpt.fish.core.ble.output.data.ManualActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.contract.DeviceDetailContract;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.IView;

import java.util.List;

import io.reactivex.Flowable;

import static com.ypcxpt.fish.core.app.Constant.NO_DATA;
import static com.ypcxpt.fish.core.ble.output.data.BaseActionData.ACTION_CODE_POWER;
import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.LONG_CLICK_DELAY;
import static com.ypcxpt.fish.core.ble.output.data.ManualActionData.get3DStrengthList;
import static com.ypcxpt.fish.core.ble.output.data.ManualActionData.getAirIntensityList;
import static com.ypcxpt.fish.core.ble.output.data.ManualActionData.getIndividualList;
import static com.ypcxpt.fish.core.ble.output.data.ManualActionData.getLongClickIndividualList;
import static com.ypcxpt.fish.core.ble.output.data.ManualActionData.getPressurePartList;

public class DeviceDetailPresenter extends BaseBLEPresenter<DeviceDetailContract.View> implements DeviceDetailContract.Presenter {
    /* 是否开机 */
    private boolean isPowerOn;
    /* 当前气囊档位，没有默认值 */
    private int mCurrAirIntensity;
    /* 当前3D力度，有默认值 */
    private int mCurr3DStrength;

    private DataSource mDS;

    public DeviceDetailPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void init(IView view) {
        super.init(view);
    }

    @Override
    public void openDataFetching() {
        ThreadHelper.postDelayed(() -> startConnect(), 600);
    }

    @Override
    protected void writeData(int code) {
        if (ACTION_CODE_POWER == code) {
            super.writeData(code);
        } else if (isPowerOn) {
            super.writeData(code);
        }
    }

    @Override
    public void onWriteSuccess(int code) {
        super.onWriteSuccess(code);
    }

    @Override
    public void onNotificationReceived(String content) {
        super.onNotificationReceived(content);
        DeviceDataParser parser = DeviceDataParser.get();
        List<Integer> data = parser.parse(content);
        /* 机器状态 */
        readDeviceState(parser, data);
        /* 时间 */
        readTime(parser, data);
        /* 自动程序 */
        readAutoMode(parser, data);
        /* 手法 */
        readTechniqueMode(parser, data);
        /* 背滚位置 和 气囊位置 */
        readBeiGun$AirScrPlace(parser, data);
        /* 3D力度 */
        read3DStrength(parser, data);
        /* 加热/脚底滚轮/腿肚滚轮/音乐蓝牙 */
        readAssistFunctions(parser, data);
        /* 零重力 */
        readZeroGravityLevel(parser, data);
        /* 按摩强度 和 机芯幅度 */
        readGearLevel1(parser, data);
        /* 气囊强度 */
        readGearLevel2(parser, data);
        /* 滚轮速度 */
        readGearLevel3(parser, data);
    }

    /* 自动程序 */
    private void readAutoMode(DeviceDataParser parser, List<Integer> data) {
        int autoMode3 = parser.getAuto3Mode(data);
        int autoMode4 = parser.getAuto4Mode(data);
        mView.displayAutoMode(autoMode3, autoMode4);
    }

    /* 机器状态 */
    private void readDeviceState(DeviceDataParser parser, List<Integer> data) {
        List<Boolean> deviceState = parser.getDeviceState(data);
        isPowerOn = deviceState.get(0);
        if (mView != null) {
            mView.onConnectSuccess();
            mView.displayDeviceState(deviceState);
        }
    }

    /* 时间 */
    private void readTime(DeviceDataParser parser, List<Integer> data) {
        String time = parser.getTime(data);
        mView.displayTime(time);
    }

    /* 手法 */
    private void readTechniqueMode(DeviceDataParser parser, List<Integer> data) {
        int techniqueMode = parser.getTechniqueMode(data);
        mView.displayTechniqueMode(techniqueMode);
    }

    /* 背滚位置 和 气囊位置 */
    private void readBeiGun$AirScrPlace(DeviceDataParser parser, List<Integer> data) {
        List<Integer> result = parser.getBeiGun$AirScrPlace(data);
        mView.display$AirScrPlace(result);
    }

    /* 3D力度 */
    private void read3DStrength(DeviceDataParser parser, List<Integer> data) {
        mCurr3DStrength = parser.get3DStrength(data);
        mView.display3DStrength(mCurr3DStrength);
    }

    /* 零重力 */
    private void readZeroGravityLevel(DeviceDataParser parser, List<Integer> data) {
        int zeroGravityLevel = parser.getZeroGravityLevel(data);
        mView.displayZeroGravity(zeroGravityLevel);
    }

    /* 加热/脚底滚轮/腿肚滚轮/音乐蓝牙 */
    private void readAssistFunctions(DeviceDataParser parser, List<Integer> data) {
        List<Boolean> assistFunctions = parser.getAssistFunctions(data);
        mView.displayAssistFunctions(assistFunctions);
    }

    /* 按摩强度 和 机芯幅度  */
    private void readGearLevel1(DeviceDataParser parser, List<Integer> data) {
        List<Integer> result = parser.getGearLevel1(data);
        mView.displayGearLevel1(result);
    }

    /* 滚轮速度 */
    private void readGearLevel3(DeviceDataParser parser, List<Integer> data) {
        int gearLevel3 = parser.getGearLevel3(data);
        mView.displayGearLevel3(gearLevel3);
    }

    /* 气囊强度 */
    private void readGearLevel2(DeviceDataParser parser, List<Integer> data) {
        mCurrAirIntensity = parser.getGearLevel2(data);
        mView.displayGearLevel2(mCurrAirIntensity);
    }

    @Override
    public void togglePower() {
        writeData(ManualActionData.getPowerAction());
    }

    @Override
    public void togglePause() {
        writeData(ManualActionData.getPauseAction());
    }

    @Override
    public void changeAirIntensity(boolean add) {
        if (mCurrAirIntensity == NO_DATA) {
            return;
        }
        int targetLevel = mCurrAirIntensity;
        targetLevel = add ? ++targetLevel : --targetLevel;
        if (targetLevel < 0) {
            Toaster.showLong("已是最小档位");
            return;
        } else if (targetLevel > DeviceConstant.AIR_INTENSITY_MAX_LEVEL - 1) {
            Toaster.showLong("已是最大档位");
            return;
        }
        int action = getAirIntensityList().get(targetLevel).code;
        writeData(action);
    }

    @Override
    public void change3DStrength(boolean add) {
        if (mCurr3DStrength == NO_DATA) {
            return;
        }
        int targetLevel = mCurr3DStrength;
        targetLevel = add ? ++targetLevel : --targetLevel;
        if (targetLevel < 0) {
            Toaster.showLong("已是最小档位");
            return;
        } else if (targetLevel > DeviceConstant.STRENGTH_3D_MAX_LEVEL - 1) {
            Toaster.showLong("已是最大档位");
            return;
        }
        int action = get3DStrengthList().get(targetLevel).code;
        writeData(action);
    }

    @Override
    public void startBackAltitude(boolean up) {
        List<DeviceAction> list = getLongClickIndividualList();
        DeviceAction deviceAction = up ? list.get(0) : list.get(1);
        writeData(deviceAction.code);
    }

    @Override
    public void endBackAltitude(boolean up) {
        ThreadHelper.postDelayed(() -> {
//            startBackAltitude(up);
            writeData(93);
        }, LONG_CLICK_DELAY);
    }

    @Override
    public void startLegAltitude(boolean up) {
        List<DeviceAction> list = getLongClickIndividualList();
        DeviceAction deviceAction = up ? list.get(2) : list.get(3);
        writeData(deviceAction.code);
    }

    @Override
    public void endLegAltitude(boolean up) {
        ThreadHelper.postDelayed(() -> {
//            startLegAltitude(up);
            writeData(94);
        }, LONG_CLICK_DELAY);
    }

    @Override
    public void startLegFlex(boolean stretch) {
        List<DeviceAction> list = getLongClickIndividualList();
        DeviceAction deviceAction = stretch ? list.get(4) : list.get(5);
        writeData(deviceAction.code);
    }

    @Override
    public void endLegFlex(boolean stretch) {
        ThreadHelper.postDelayed(() -> startLegFlex(stretch), LONG_CLICK_DELAY);
    }

    @Override
    public void onBackAltitude(boolean up) {
        startBackAltitude(up);
        endBackAltitude(up);
    }

    @Override
    public void onLegAltitude(boolean up) {
        startLegAltitude(up);
        endLegAltitude(up);
    }

    @Override
    public void ontLegFlex(boolean stretch) {
        startLegFlex(stretch);
        endLegFlex(stretch);
    }

    @Override
    public void toggleJiaoGun() {
        /**
         * 脚滚和腿滚测试发现反了
         */
        writeData(getIndividualList().get(0).code);
//        writeData(getIndividualList().get(2).code);
    }

    @Override
    public void toggleTuiGun() {
        /**
         * 脚滚和腿滚测试发现反了
         */
        writeData(getIndividualList().get(2).code);
//        writeData(getIndividualList().get(0).code);
    }

    @Override
    public void onGlobalFailure(Throwable throwable) {
        super.onGlobalFailure(throwable);
        if (mView != null) {
            mView.onConnectFailure();
        }
    }

    @Override
    public void toggleZeroGravity() {
        writeData(ManualActionData.ZERO_GRAVITY_CODE);
    }

    @Override
    public void toggleHeat() {
        writeData(getIndividualList().get(1).code);
    }

    //气压腰间
    @Override
    public void toggleQiyayaojian() {
        Logger.e("气压腰肩", "writeData-->" + getPressurePartList().get(0).code);
        writeData(getPressurePartList().get(0).code);
    }

    @Override
    public void toggleQiyatunbu() {
        Logger.e("气压臀部", "writeData-->" + getPressurePartList().get(1).code);
        writeData(getPressurePartList().get(1).code);
    }

    @Override
    public void toggleQiyashoubu() {
        Logger.e("气压手部", "writeData-->" + getPressurePartList().get(2).code);
        writeData(getPressurePartList().get(2).code);
    }

    @Override
    public void toggleQiyatuibu() {
        Logger.e("气压腿部", "writeData-->" + getPressurePartList().get(3).code);
        writeData(getPressurePartList().get(3).code);
    }

    @Override
    public void zeroGravity(int zeroCode) {
        if (zeroCode == -1) {
            Logger.e("为-1", "执行1档");
            writeData(ManualActionData.getZeroGravityList().get(0).code);//1挡
        } else if (zeroCode == 0) {
            Logger.e("为0", "执行2档");
            writeData(ManualActionData.getZeroGravityList().get(1).code);//2挡
        } else if (zeroCode == 1) {
            Logger.e("为1", "执行3档");
            writeData(ManualActionData.getZeroGravityList().get(2).code);//3挡
        } else if (zeroCode == 2) {
            Logger.e("为2", "执行复位档");
            writeData(ManualActionData.getZeroGravityList().get(2).code);//3挡
        }
    }

    @Override
    public void useDevice(String mac) {
        Flowable<Object> source = mDS.getControlDevice(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(true)
                .onSuccess(o -> {
            Logger.i("使用设备", "记录一次");
        }).start();
    }


}
