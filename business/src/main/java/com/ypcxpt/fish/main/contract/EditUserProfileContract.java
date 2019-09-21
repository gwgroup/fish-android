package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.RegionInfo;

import java.util.List;

public interface EditUserProfileContract {
    interface View extends IView{
        void onUpdateSuccess(UserProfile newProfile);
        void showAddressRegion(List<RegionInfo> regionInfos);
    }

    interface Presenter extends IPresenter {

        List<PopupItem> getGenderData();

        void saveProfile();

        /* 个人信息是否修改过 */
        boolean isProfileChanged();

        void selectAddress(String parentCode);
    }
}
