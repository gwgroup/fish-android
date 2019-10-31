package com.ypcxpt.fish.main.presenter;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.EditUserProfileContract;
import com.ypcxpt.fish.main.model.RegionInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public class EditUserProfilePresenter extends BasePresenter<EditUserProfileContract.View> implements EditUserProfileContract.Presenter {
    private DataSource mDS;

    private UserProfile mUserProfile;

    private String mInitProfileFlag;

    public EditUserProfilePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void openDataFetching() {
        mInitProfileFlag = mUserProfile.toString();
    }

    @Override
    public List<PopupItem> getGenderData() {
        List<PopupItem> data = new ArrayList<>();
        data.add(new PopupItem("男"));
        data.add(new PopupItem("女"));
        return data;
    }

    @Override
    public void saveProfile() {
        Flowable<UserProfile> source = mDS.updateUserProfile(mUserProfile);
        fetch(source)
                .onSuccess(newProfile -> {
                    Logger.d("CCC", newProfile.toString());
                    mView.onUpdateSuccess(newProfile);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public boolean isProfileChanged() {
        if (mInitProfileFlag == null) {
            return false;
        }
        return !mInitProfileFlag.equals(mUserProfile.toString());
    }

    @Override
    public void selectAddress(String parentCode) {
//        Flowable<List<RegionInfo>> source = mDS.getRegion(parentCode);
//        fetch(source)
//                .onSuccess(regionInfos -> {
//                    Logger.d("CCC", "regionInfos-->" + regionInfos.toString());
//                    mView.showAddressRegion(regionInfos);
//                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }

}
