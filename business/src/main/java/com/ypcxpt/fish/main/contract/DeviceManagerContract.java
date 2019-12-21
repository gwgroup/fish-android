package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

public interface DeviceManagerContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter {
        void getDevices();
    }
}
