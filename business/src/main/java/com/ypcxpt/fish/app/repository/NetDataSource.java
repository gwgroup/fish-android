package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.device.model.DataHistory;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.net.NetManager;
import com.ypcxpt.fish.library.net.request.ParamBuilder;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoStatusAll;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.main.model.VersionDetailInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

public class NetDataSource implements DataSource {
    private ApiService mApiService;

    public NetDataSource() {
        mApiService = NetManager.getInstance().createAPIService(ApiService.class);
    }

    @Override
    public Flowable<Object> getVerifyCode(String phoneNo) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("mobile", phoneNo)
                .build();
        return mApiService.getVerifyCode(param);
    }

    @Override
    public Flowable<LoginResult> login(String phoneNo, String verifyCode) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("mobile", phoneNo)
                .put("vali_code", verifyCode)
                .build();
        return mApiService.login(param);
    }

    @Override
    public Flowable<UserProfile> getUserProfile() {
        return mApiService.getUserProfile(AppData.token());
    }

    @Override
    public Flowable<List<Scenes>> getScenes() {
        return mApiService.getScenes(AppData.token());
    }

    @Override
    public Flowable<List<IoInfo>> getIoInfo(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.getIoInfo(AppData.token(), param);
    }

    @Override
    public Flowable<IoStatusAll> getIoStatus(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.getIoStatus(AppData.token(), param);
    }

    @Override
    public Flowable<Object> openIO(String mac, String code, int duration) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("client_id", mac)
                .put("io_code", code)
                .put("duration", duration)
                .build();
        return mApiService.openIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> closeIO(String mac, String code) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("client_id", mac)
                .put("io_code", code)
                .build();
        return mApiService.closeIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> addScenes(String mac, String name) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("scene_name", name)
                .build();
        return mApiService.addScenes(AppData.token(), param);
    }

    @Override
    public Flowable<Object> removeScenes(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.removeScenes(AppData.token(), param);
    }

    @Override
    public Flowable<Object> renameScenes(String mac, String name) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("scene_name", name)
                .build();
        return mApiService.renameScenes(AppData.token(), param);
    }

    @Override
    public Flowable<LoginResult> bindPhone(String phoneNo, String verifyCode, String openid) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("mobile", phoneNo)
                .put("vali_code", verifyCode)
                .put("openid", openid)
                .build();
        return mApiService.bindPhone(param);
    }

    @Override
    public Flowable<UserProfile> updateUserProfile(UserProfile userProfile) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
//                .put("mobile", userProfile.phoneNo)
//                .put("sex", userProfile.gender + "")
//                .put("card_id", userProfile.idNumber)
//                .put("icon", userProfile.avatar)
//                .put("name", userProfile.name)
//                .put("height", userProfile.height + "")
//                .put("weight", userProfile.weight + "")
//                .put("birthday", userProfile.birthday)
//                .put("address", userProfile.address)
//                .put("region_code", userProfile.region_code)
                .build();
        return mApiService.updateUserProfile(AppData.token(), param);
    }

    @Override
    public Flowable<Object> feedback(String content) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("content", content)
                .build();
        return mApiService.feedback(AppData.token(), param);
    }



    @Override
    public Flowable<WeatherInfo> getWeatherInfo(double lat, double lng) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("lat", lat + "")
                .put("lng", lng + "")
                .build();
        return mApiService.getWeatherInfo(AppData.token(), param);
    }

    @Override
    public Flowable<VersionDetailInfo> getVersion(String versionCode) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("versionCode", versionCode + "")
                .build();
        return mApiService.getVersion(AppData.token(), param);
    }

    @Override
    public Flowable<CollectionInfo> getCollections(String pageIndex, String pageNum) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("page", pageIndex)
                .put("rowsPerPage", pageNum)
                .build();
        return mApiService.getCollections(AppData.token(), param);
    }

    @Override
    public Flowable<NotificationInfo> getNotifications(String pageIndex, String pageNum) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("page", pageIndex)
                .put("rowsPerPage", pageNum)
                .build();
        return mApiService.getNotifications(AppData.token(), param);
    }

    @Override
    public Flowable<CommentInfo> getMyComments(String pageIndex, String pageNum) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("page", pageIndex)
                .put("rowsPerPage", pageNum)
                .build();
        return mApiService.getMyComments(AppData.token(), param);
    }

    @Override
    public Flowable<CommentInfo> getCommentsWithMe(String pageIndex, String pageNum) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("page", pageIndex)
                .put("rowsPerPage", pageNum)
                .build();
        return mApiService.getCommentsWithMe(AppData.token(), param);
    }

    @Override
    public Flowable<Object> getControlDevice(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("mac", mac)
                .build();
        return mApiService.getControlDevice(AppData.token(), param);
    }

    @Override
    public Flowable<Object> deviceActionLog(List<DataHistory> historyList) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("logs", historyList)
                .build();
        return mApiService.deviceActionLog(AppData.token(), param);
    }
}
