package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.model.IoTrigger;

import java.util.List;

public interface TimingPlanContract {
    interface View extends IView {
        void showScenes(List<Scenes> scenes);

        void showIoPlans(List<IoPlan> ioPlans);

        void showIoTriggers(List<IoTrigger> ioTriggers);
    }

    interface Presenter extends IPresenter {
        void getScenes();

        void getAllPlans(String mac);
        void openPlan(String mac, String planId);
        void closePlan(String mac, String planId);

        void getAllTriggers(String mac);
        void openTrigger(String mac, String planId);
        void closeTrigger(String mac, String planId);
    }
}
