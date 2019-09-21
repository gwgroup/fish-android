package com.ypcxpt.fish.device.contract;

import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

import java.util.List;

public interface ShellchairContract {
    interface View extends IView {

        BaseBLEPresenter getBLEPresenter();

        void displayDeviceState(List<Boolean> data);

        void displayTime(String time);

        void displayAutoMode(int data3, int data4);

        void displayTechniqueMode(int data);

        void display3DStrength(List<Integer> data);

        void displayZeroGravity(int data);

        void display$AirScrPlace(List<Boolean> data);

        void displayAssistFunctions(List<Boolean> data);

        void displayGearLevel1(List<Integer> data);

        void displayGearLevel2(List<Boolean> data);

        void displayGearLevel3(int data);

        void onConnectSuccess();

        void onConnectFailure();

    }

    interface Presenter extends IPresenter {

        void togglePower();

        void togglePause();

        void changeAirIntensity(boolean add);

        void change3DStrength(boolean add);

        void changeWidth(boolean add);

        void startBackAltitude(boolean up);
        void endBackAltitude(boolean up);

        void startLegAltitude(boolean up);
        void endLegAltitude(boolean up);

        void startLegFlex(boolean stretch);
        void endLegFlex(boolean stretch);

        void onBackAltitude(boolean up);

        void onLegAltitude(boolean up);

        void ontLegFlex(boolean stretch);

        void toggleJiaoGun();
        void toggleTuiGun();
        void toggleHeat();
        void zeroGravity(int zeroCode);

        void useDevice(String mac);

        void togglePosition();//定点
        void toggleTimer();//时间设置

    }

}