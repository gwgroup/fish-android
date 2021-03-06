package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.main.model.Scenes;
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
import com.ypcxpt.fish.main.model.VersionDetailInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    /* 发送验证码. */
    @POST("api/user/send_vali_sms")
    Flowable<Object> getVerifyCode(@Body HashMap<String, Object> param);

    /* 登录 */
    @POST("api/user/login")
    Flowable<LoginResult> login(@Body HashMap<String, Object> param);

    /* 获取用户信息 */
    @POST("api/user/load")
    Flowable<UserProfile> getUserProfile(@Header("authorization") String token);

    /* 获取所有场景 */
    @POST("api/scene/get_all_scene")
    Flowable<List<Scenes>> getScenes(@Header("authorization") String token);

    /* 获取设备IO配置信息 */
    @POST("api/device/get_io_info")
    Flowable<List<IoInfo>> getIoInfo(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 启用设备IO */
    @POST("api/device/io_enable")
    Flowable<Object> enableIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 禁用设备IO */
    @POST("api/device/io_disable")
    Flowable<Object> disableIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 重命名设备IO */
    @POST("api/device/io_rename")
    Flowable<Object> renameIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 设置功耗设备IO */
    @POST("api/device/power")
    Flowable<Object> setPowerIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 投喂机校准设备IO */
    @POST("api/device/calibration_feeder")
    Flowable<Object> calibrationFeederIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取设备IO状态信息 */
    @POST("api/device/get_device_status")
    Flowable<IoStatusAll> getIoStatus(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 打开设备IO */
    @POST("api/device/open")
    Flowable<Object> openIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 关闭设备IO */
    @POST("api/device/close")
    Flowable<Object> closeIO(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 添加场景 */
    @POST("api/scene/add_scene")
    Flowable<Object> addScenes(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 移除场景 */
    @POST("api/scene/remove_scene")
    Flowable<Object> removeScenes(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 重命名场景 */
    @POST("api/scene/rename_scene")
    Flowable<Object> renameScenes(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取所有定时计划 */
    @POST("api/plan/get_all_plan")
    Flowable<List<IoPlan>> getAllPlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 启用定时计划 */
    @POST("api/plan/enable_plan")
    Flowable<Object> openPlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 禁用定时计划 */
    @POST("api/plan/disable_plan")
    Flowable<Object> closePlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 删除定时计划 */
    @POST("api/plan/remove_plan")
    Flowable<Object> deletePlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 添加定时计划 */
    @POST("api/plan/add_plan")
    Flowable<Object> addPlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 编辑定时计划 */
    @POST("api/plan/edit_plan")
    Flowable<Object> editPlan(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取所有触发任务 */
    @POST("api/trigger/get_all_trigger")
    Flowable<List<IoTrigger>> getAllTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 启用触发任务 */
    @POST("api/trigger/enable_trigger")
    Flowable<Object> openTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 禁用触发任务 */
    @POST("api/trigger/disable_trigger")
    Flowable<Object> closeTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 删除触发任务 */
    @POST("api/trigger/remove_trigger")
    Flowable<Object> deleteTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 添加触发任务 */
    @POST("api/trigger/add_trigger")
    Flowable<Object> addTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 编辑触发任务 */
    @POST("aapi/trigger/edit_trigger")
    Flowable<Object> editTrigger(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取摄像头配置 */
    @POST("api/cams/get_config")
    Flowable<Cams> getCamsConfig(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取加密摄像头配置 */
    @POST("api/cams/auth")
    Flowable<Cams> getCamsConfigAuth(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 请求推流 */
    @POST("api/cams/play")
    Flowable<Object> doPlay(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 切换清晰度 */
    @POST("api/cams/switch_profile")
    Flowable<Object> switchProfile(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 移动镜头 */
    @POST("api/cams/move")
    Flowable<Object> doCamsMove(@Header("authorization") String token, @Body HashMap<String, Object> param);


    /* 绑定手机号 */
    @POST("weixin_bind")
    Flowable<LoginResult> bindPhone(@Body HashMap<String, Object> param);

    /* 修改用户信息 */
    @POST("edit_user")
    Flowable<UserProfile> updateUserProfile(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 意见反馈 */
    @POST("opinion")
    Flowable<Object> feedback(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 查询天气 */
    @POST("get_weather_v2")
    Flowable<WeatherInfo> getWeatherInfo(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 版本升级(废弃了) */
    @POST("version")
    Flowable<VersionDetailInfo> getVersion(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取收藏的文章列表 */
    @POST("article/get_star_articles")
    Flowable<CollectionInfo> getCollections(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取通知列表 */
    @POST("notice/list")
    Flowable<NotificationInfo> getNotifications(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取我的列表 */
    @POST("article/my_comments")
    Flowable<CommentInfo> getMyComments(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 获取@我的列表 */
    @POST("article/comments_with_me")
    Flowable<CommentInfo> getCommentsWithMe(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 使用设备记录一下 */
    @POST("control_device")
    Flowable<Object> getControlDevice(@Header("authorization") String token, @Body HashMap<String, Object> param);
}
