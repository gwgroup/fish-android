package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.net.NetManager;
import com.ypcxpt.fish.library.net.request.ParamBuilder;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.Cams;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoPlan;
import com.ypcxpt.fish.main.model.IoStatusAll;
import com.ypcxpt.fish.main.model.IoTrigger;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.main.model.PlanParam;
import com.ypcxpt.fish.main.model.TriggerParam;
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
    public Flowable<Object> enableIO(String mac, String code) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("code", code)
                .build();
        return mApiService.enableIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> disableIO(String mac, String code) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("code", code)
                .build();
        return mApiService.disableIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> renameIO(String mac, String code, String name) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("code", code)
                .put("name", name)
                .build();
        return mApiService.renameIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> setPowerIO(String mac, String code, int power) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("code", code)
                .put("power_w", power)
                .build();
        return mApiService.setPowerIO(AppData.token(), param);
    }

    @Override
    public Flowable<Object> calibrationFeederIO(String mac, String code, double feeder) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("code", code)
                .put("weight_per_second", feeder)
                .build();
        return mApiService.calibrationFeederIO(AppData.token(), param);
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
    public Flowable<List<IoPlan>> getAllPlan(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.getAllPlan(AppData.token(), param);
    }

    @Override
    public Flowable<Object> openPlan(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.openPlan(AppData.token(), param);
    }

    @Override
    public Flowable<Object> closePlan(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.closePlan(AppData.token(), param);
    }

    @Override
    public Flowable<Object> deletePlan(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.deletePlan(AppData.token(), param);
    }

    @Override
    public Flowable<Object> addPlan(String mac, PlanParam planParam) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("plan", planParam)
                .build();
        return mApiService.addPlan(AppData.token(), param);
    }

    @Override
    public Flowable<Object> editPlan(String mac, PlanParam planParam) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("plan", planParam)
                .build();
        return mApiService.editPlan(AppData.token(), param);
    }

    @Override
    public Flowable<List<IoTrigger>> getAllTrigger(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.getAllTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Object> openTrigger(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.openTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Object> closeTrigger(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.closeTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Object> deleteTrigger(String mac, String id) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("id", id)
                .build();
        return mApiService.deleteTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Object> addTrigger(String mac, TriggerParam planParam) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("trigger", planParam)
                .build();
        return mApiService.addTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Object> editTrigger(String mac, TriggerParam planParam) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("trigger", planParam)
                .build();
        return mApiService.editTrigger(AppData.token(), param);
    }

    @Override
    public Flowable<Cams> getCamsConfig(String mac) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .build();
        return mApiService.getCamsConfig(AppData.token(), param);
    }

    @Override
    public Flowable<Cams> getCamsConfigAuth(String mac, String key, String pass) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("cam_key", key)
                .put("password", pass)
                .build();
        return mApiService.getCamsConfigAuth(AppData.token(), param);
    }

    @Override
    public Flowable<Object> doPlay(String mac, String key) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("cam_key", key)
                .build();
        return mApiService.doPlay(AppData.token(), param);
    }

    @Override
    public Flowable<Object> switchProfile(String mac, String key, String profileToken) {
        HashMap<String, Object> param = ParamBuilder.newBuilder()
                .put("device_mac", mac)
                .put("cam_key", key)
                .put("profile_token", profileToken)
                .build();
        return mApiService.switchProfile(AppData.token(), param);
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
}
