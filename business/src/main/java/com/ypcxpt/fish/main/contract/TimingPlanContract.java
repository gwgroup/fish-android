package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.IoPlan;

import java.util.List;

public interface TimingPlanContract {
    interface View extends IView {
        void showScenes(List<Scenes> scenes);

        void showIoPlans(List<IoPlan> ioPlans);
    }

    interface Presenter extends IPresenter {
        void getScenes();

        void getAllPlans(String mac);
    }
}
