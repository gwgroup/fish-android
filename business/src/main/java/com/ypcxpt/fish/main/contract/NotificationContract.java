package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.NotificationInfo;

public interface NotificationContract {
    interface View extends IView {
        void showNotifications(NotificationInfo notificationInfo);
    }

    interface Presenter extends IPresenter {
        void getNotifications(String pageIndex, String pageNum);
    }
}
