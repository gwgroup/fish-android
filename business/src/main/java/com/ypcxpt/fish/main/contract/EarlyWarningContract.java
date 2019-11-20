package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

import java.util.List;

public interface EarlyWarningContract {
    interface View extends IView {
        void showScenes(List<Scenes> scenes);
    }

    interface Presenter extends IPresenter {
        void getScenes();
    }
}
