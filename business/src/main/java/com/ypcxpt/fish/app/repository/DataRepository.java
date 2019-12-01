package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.device.model.DataHistory;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.model.IoStatusAll;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.main.model.VersionDetailInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.List;

import io.reactivex.Flowable;

public class DataRepository implements DataSource {
    private NetDataSource mNetDS;

    public DataRepository() {
        mNetDS = new NetDataSource();
    }

    @Override
    public Flowable<Object> getVerifyCode(String phoneNo) {
        return mNetDS.getVerifyCode(phoneNo);
    }

    @Override
    public Flowable<LoginResult> login(String phoneNo, String verifyCode) {
        return mNetDS.login(phoneNo, verifyCode);
    }

    @Override
    public Flowable<UserProfile> getUserProfile() {
        return mNetDS.getUserProfile();
    }

    @Override
    public Flowable<List<Scenes>> getScenes() {
        return mNetDS.getScenes();
    }

    @Override
    public Flowable<List<IoInfo>> getIoInfo(String mac) {
        return mNetDS.getIoInfo(mac);
    }

    @Override
    public Flowable<IoStatusAll> getIoStatus(String mac) {
        return mNetDS.getIoStatus(mac);
    }

    @Override
    public Flowable<Object> openIO(String mac, String code, int duration) {
        return mNetDS.openIO(mac, code, duration);
    }

    @Override
    public Flowable<Object> closeIO(String mac, String code) {
        return mNetDS.closeIO(mac, code);
    }

    @Override
    public Flowable<Object> addScenes(String mac, String name) {
        return mNetDS.addScenes(mac, name);
    }

    @Override
    public Flowable<Object> removeScenes(String mac) {
        return mNetDS.removeScenes(mac);
    }

    @Override
    public Flowable<Object> renameScenes(String mac, String name) {
        return mNetDS.renameScenes(mac, name);
    }

    @Override
    public Flowable<List<IoPlan>> getAllPlan(String mac) {
        return mNetDS.getAllPlan(mac);
    }





    @Override
    public Flowable<LoginResult> bindPhone(String phoneNo, String verifyCode, String openid) {
        return mNetDS.bindPhone(phoneNo, verifyCode, openid);
    }





    @Override
    public Flowable<UserProfile> updateUserProfile(UserProfile userProfile) {
        return mNetDS.updateUserProfile(userProfile);
    }

    @Override
    public Flowable<Object> feedback(String content) {
        return mNetDS.feedback(content);
    }

    @Override
    public Flowable<WeatherInfo> getWeatherInfo(double lat, double lng) {
        return mNetDS.getWeatherInfo(lat, lng);
    }

    @Override
    public Flowable<VersionDetailInfo> getVersion(String versionCode) {
        return mNetDS.getVersion(versionCode);
    }

    @Override
    public Flowable<CollectionInfo> getCollections(String pageIndex, String pageNum) {
        return mNetDS.getCollections(pageIndex, pageNum);
    }

    @Override
    public Flowable<NotificationInfo> getNotifications(String pageIndex, String pageNum) {
        return mNetDS.getNotifications(pageIndex, pageNum);
    }

    @Override
    public Flowable<CommentInfo> getMyComments(String pageIndex, String pageNum) {
        return mNetDS.getMyComments(pageIndex, pageNum);
    }

    @Override
    public Flowable<CommentInfo> getCommentsWithMe(String pageIndex, String pageNum) {
        return mNetDS.getCommentsWithMe(pageIndex, pageNum);
    }

    @Override
    public Flowable<Object> getControlDevice(String mac) {
        return mNetDS.getControlDevice(mac);
    }

    @Override
    public Flowable<Object> deviceActionLog(List<DataHistory> historyList) {
        return mNetDS.deviceActionLog(historyList);
    }
}
