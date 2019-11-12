package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.NotificationInfo;

import java.util.List;

public interface IoConfigContract {
    interface View extends IView {
        void showIoInfos(List<IoInfo> ioInfos);
    }

    interface Presenter extends IPresenter {
    }
}
