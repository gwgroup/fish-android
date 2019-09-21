package com.ypcxpt.fish.login.presenter;

import android.os.CountDownTimer;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.login.contract.AdvertisingContract;

import static com.ypcxpt.fish.core.app.Constant.ADVERTISE_TIME;

public class AdvertisingPresenter extends BasePresenter<AdvertisingContract.View> implements AdvertisingContract.Presenter {

    public static final int COUNT_DOWN_INTERVAL = 200;

    private DataSource mDS;

    public AdvertisingPresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void openDataFetching() {
        mTimer.start();
    }

    private CountDownTimer mTimer = new CountDownTimer(ADVERTISE_TIME, COUNT_DOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            long untilFinishedSeconds = millisUntilFinished / 1000;
            mView.onCountDownTick((int) untilFinishedSeconds);
        }

        @Override
        public void onFinish() {
            mView.onCountDownFinish();
        }
    };

    @Override
    public void cancelCountDown() {
        destroyTimer();
    }

    @Override
    public void onDestroy() {
        destroyTimer();
        super.onDestroy();
    }

    private void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
