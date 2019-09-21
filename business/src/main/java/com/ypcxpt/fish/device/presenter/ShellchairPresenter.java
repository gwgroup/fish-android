package com.ypcxpt.fish.device.presenter;

import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.ble.input.ShellchairDataParser;
import com.ypcxpt.fish.core.ble.output.data.ShellManualActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.contract.ShellchairContract;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.IView;

import java.util.List;

import io.reactivex.Flowable;

import static com.ypcxpt.fish.core.app.Constant.NO_DATA;
import static com.ypcxpt.fish.core.ble.output.data.BaseActionData.ACTION_CODE_POWER;
import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.LONG_CLICK_DELAY;
import static com.ypcxpt.fish.core.ble.output.data.ShellManualActionData.getIndividualList;
import static com.ypcxpt.fish.core.ble.output.data.ShellManualActionData.getLongClickIndividualList;

public class ShellchairPresenter extends BaseBLEPresenter<ShellchairContract.View> implements ShellchairContract.Presenter {
    /* 是否开机 */
    private boolean isPowerOn;
    /* 当前气囊档位，没有默认值 */
    private int mCurrAirIntensity;
    private List<Boolean> mCurrAir;
    /* 当前速度，有默认值 */
    private int mSpeed;
    /* 当前宽度，有默认值 */
    private int mWidth;

    private DataSource mDS;

    public ShellchairPresenter() {
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
        ShellchairDataParser parser = ShellchairDataParser.get();
        List<Integer> data = parser.parse(content);
        /* 机器状态 */
        readDeviceState(parser, data);
        /* 时间 */
        readTime(parser, data);
        /* 自动程序 */
        readAutoMode(parser, data);
        /* 手法 */
        readTechniqueMode(parser, data);
        /* 速度 和 宽度 */
        readGearLevel1(parser, data);
        /* 气囊强度 */
        readGearLevel2(parser, data);
        /* 零重力 */
        readZeroGravityLevel(parser, data);
        /* 背滚位置 */
        readBeiGun$AirScrPlace(parser, data);
        /* 气囊位置 */
        read3DStrength(parser, data);
        /* 加热/脚底滚轮/腿肚滚轮/音乐蓝牙 */
        readAssistFunctions(parser, data);
        /* 滚轮速度 */
        readGearLevel3(parser, data);
    }

    /* 自动程序 */
    private void readAutoMode(ShellchairDataParser parser, List<Integer> data) {
        int autoMode3 = parser.getAuto3Mode(data);
        int autoMode4 = parser.getAuto4Mode(data);
        mView.displayAutoMode(autoMode3, autoMode4);
    }

    /* 机器状态 */
    private void readDeviceState(ShellchairDataParser parser, List<Integer> data) {
        List<Boolean> deviceState = parser.getDeviceState(data);
        isPowerOn = deviceState.get(0);
        if (mView != null) {
            mView.onConnectSuccess();
            mView.displayDeviceState(deviceState);
        }
    }

    /* 时间 */
    private void readTime(ShellchairDataParser parser, List<Integer> data) {
        String time = parser.getTime(data);
        mView.displayTime(time);
    }

    /* 手法 */
    private void readTechniqueMode(ShellchairDataParser parser, List<Integer> data) {
        int techniqueMode = parser.getTechniqueMode(data);
        mView.displayTechniqueMode(techniqueMode);
    }

    /* 背滚位置 */
    private void readBeiGun$AirScrPlace(ShellchairDataParser parser, List<Integer> data) {
        List<Boolean> result = parser.getBeiGun$AirScrPlace(data);
        mView.display$AirScrPlace(result);
    }

    /* 3D力度-->气囊位置 */
    private void read3DStrength(ShellchairDataParser parser, List<Integer> data) {
//        mCurr3DStrength = parser.get3DStrength(data);
//        mView.display3DStrength(mCurr3DStrength);
        List<Integer> result = parser.getAirScrPlace(data);
        mView.display3DStrength(result);
    }

    /* 零重力 */
    private void readZeroGravityLevel(ShellchairDataParser parser, List<Integer> data) {
        int zeroGravityLevel = parser.getZeroGravityLevel(data);
        mView.displayZeroGravity(zeroGravityLevel);
    }

    /* 加热/脚底滚轮/腿肚滚轮/音乐蓝牙 */
    private void readAssistFunctions(ShellchairDataParser parser, List<Integer> data) {
        List<Boolean> assistFunctions = parser.getAssistFunctions(data);
        mView.displayAssistFunctions(assistFunctions);
    }

    /* 速度 和 宽度  */
    private void readGearLevel1(ShellchairDataParser parser, List<Integer> data) {
        List<Integer> result = parser.getGearLevel1(data);
        mSpeed = result.get(0);
        mWidth = result.get(1);
        mView.displayGearLevel1(result);
    }

    /* 滚轮速度 */
    private void readGearLevel3(ShellchairDataParser parser, List<Integer> data) {
        int gearLevel3 = parser.getGearLevel3(data);
        mView.displayGearLevel3(gearLevel3);
    }

    /* 气囊强度 */
    private void readGearLevel2(ShellchairDataParser parser, List<Integer> data) {
        mCurrAir = parser.getGearLevel2(data);
        mView.displayGearLevel2(mCurrAir);
    }

    @Override
    public void togglePower() {
        writeData(ShellManualActionData.getPowerAction());
    }

    @Override
    public void togglePause() {
        writeData(ShellManualActionData.getPauseAction());
    }

    @Override
    public void changeAirIntensity(boolean add) {
        if (ObjectUtils.isEmpty(mCurrAir)) {
            return;
        }
//        if (mCurrAirIntensity == NO_DATA) {
//            return;
//        }
//        int targetLevel = mCurrAirIntensity;
//        targetLevel = add ? ++targetLevel : --targetLevel;
//        if (targetLevel < 0) {
//            Toaster.showLong("已是最小档位");
//            return;
//        } else if (targetLevel > DeviceConstant.AIR_INTENSITY_MAX_LEVEL - 1) {
//            Toaster.showLong("已是最大档位");
//            return;
//        }
//        int action = getAirIntensityList().get(targetLevel).code;
//        writeData(action);
        if (add) {
            writeData(ShellManualActionData.AIR_ADD_CODE);
        } else {
            writeData(ShellManualActionData.AIR_MINUS_CODE);
        }
    }

    @Override
    public void change3DStrength(boolean add) {
        if (mSpeed == NO_DATA) {
            return;
        }
//        int targetLevel = mCurr3DStrength;
//        targetLevel = add ? ++targetLevel : --targetLevel;
//        if (targetLevel < 0) {
//            Toaster.showLong("已是最小档位");
//            return;
//        } else if (targetLevel > DeviceConstant.STRENGTH_3D_MAX_LEVEL - 1) {
//            Toaster.showLong("已是最大档位");
//            return;
//        }
//        int action = get3DStrengthList().get(targetLevel).code;
//        writeData(action);
        if (add) {
            writeData(ShellManualActionData.SPEED_ADD_CODE);
        } else {
            writeData(ShellManualActionData.SPEED_MINUS_CODE);
        }
    }

    @Override
    public void changeWidth(boolean add) {
        if (mWidth == NO_DATA) {
            return;
        }

        int targetLevel = mWidth - 4;
        if (add) {
            if (targetLevel == 1) {
                writeData(ShellManualActionData.getWidthList().get(1).code);
            } else if (targetLevel == 2) {
                writeData(ShellManualActionData.getWidthList().get(2).code);
            } else if (targetLevel == 3) {
                writeData(ShellManualActionData.getWidthList().get(2).code);
            }
        } else {
            if (targetLevel == 3) {
                writeData(ShellManualActionData.getWidthList().get(1).code);
            } else if (targetLevel == 2) {
                writeData(ShellManualActionData.getWidthList().get(0).code);
            } else if (targetLevel == 1) {
                writeData(ShellManualActionData.getWidthList().get(0).code);
            }
        }

//        targetLevel = add ? ++targetLevel : --targetLevel;
//        if (targetLevel < 0) {
////            Toaster.showLong("已是最小档位");
//            return;
//        } else if (targetLevel > 3 - 1) {
////            Toaster.showLong("已是最大档位");
//            return;
//        }
//        int action = ShellManualActionData.getWidthList().get(targetLevel).code;
//        writeData(action);
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
            startBackAltitude(up);
//            writeData(93);
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
            startLegAltitude(up);
//            writeData(94);
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
//        endBackAltitude(up);
    }

    @Override
    public void onLegAltitude(boolean up) {
        startLegAltitude(up);
//        endLegAltitude(up);
    }

    @Override
    public void ontLegFlex(boolean stretch) {
        startLegFlex(stretch);
        endLegFlex(stretch);
    }

    @Override
    public void toggleJiaoGun() {
        writeData(getIndividualList().get(0).code);
    }

    @Override
    public void toggleTuiGun() {
        writeData(getIndividualList().get(2).code);
    }

    @Override
    public void onGlobalFailure(Throwable throwable) {
        super.onGlobalFailure(throwable);
        if (mView != null) {
            mView.onConnectFailure();
        }
    }

    @Override
    public void togglePosition() {
        writeData(ShellManualActionData.POSITION_CODE);
    }

    @Override
    public void toggleTimer() {
        writeData(ShellManualActionData.TIMER_CODE);
    }

    @Override
    public void toggleHeat() {
        writeData(getIndividualList().get(1).code);
    }

    @Override
    public void zeroGravity(int zeroCode) {
        writeData(ShellManualActionData.ZERO_GRAVITY_CODE);
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
