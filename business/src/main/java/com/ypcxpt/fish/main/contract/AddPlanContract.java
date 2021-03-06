package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.PlanParam;

import java.util.List;

public interface AddPlanContract {
    interface View extends IView {
        void showIoInfos(List<IoInfo> ioInfos);
        void onCommitSuccess();
    }

    interface Presenter extends IPresenter {
        void addPlan(String mac, PlanParam planParam);
        void editPlan(String mac, PlanParam planParam);
        void deletePlan(String mac, String planId);
    }
}
