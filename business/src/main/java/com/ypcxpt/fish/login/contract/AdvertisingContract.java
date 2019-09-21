package com.ypcxpt.fish.login.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;

public interface AdvertisingContract {
    interface View extends IView {
        void onCountDownTick(int untilFinishedSecond);

        void onCountDownFinish();
    }

    interface Presenter extends IPresenter {
        void cancelCountDown();
    }
}
