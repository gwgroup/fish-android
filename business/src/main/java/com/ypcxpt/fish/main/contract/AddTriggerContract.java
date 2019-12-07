package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.PlanParam;
import com.ypcxpt.fish.main.model.TriggerParam;

import java.util.List;

public interface AddTriggerContract {
    interface View extends IView {
        void showIoInfos(List<IoInfo> ioInfos);
        void onCommitSuccess();
    }

    interface Presenter extends IPresenter {
        void addPlan(String mac, TriggerParam triggerParam);
        void editPlan(String mac, TriggerParam triggerParam);
        void deletePlan(String mac, String id);
    }
}
