package com.ypcxpt.fish.device.contract;

import com.ypcxpt.fish.device.presenter.BaseBLEt100Presenter;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

public interface T100Contract {
    interface View extends IView {

        BaseBLEt100Presenter getBLEPresenter();

        void displayDeviceState(int data);

        void displayNeckPositiveAndNegative(int data);

        void displayNeckUpAndDown(int data);

        void displayAutoMode(int data);

        void displayAir(int data);

        void displayHeat(int data);

        void displayTime(String time);

        void onConnectSuccess();

        void onConnectFailure();
    }

    interface Presenter extends IPresenter {

        void togglePower();

        void useDevice(String mac);
    }

}
