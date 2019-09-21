package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.main.model.RegionInfo;
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
    @POST("vali_code")
    Flowable<Object> getVerifyCode(@Body HashMap<String, Object> param);

    /* 登录 */
    @POST("login")
    Flowable<LoginResult> login(@Body HashMap<String, Object> param);

    /* 绑定手机号 */
    @POST("weixin_bind")
    Flowable<LoginResult> bindPhone(@Body HashMap<String, Object> param);

    /* 获取设备 */
    @POST("get_devices")
    Flowable<List<NetDevice>> getDevices(@Header("authorization") String token);

    /* 获取用户信息 */
    @POST("user")
    Flowable<UserProfile> getUserProfile(@Header("authorization") String token);

    /* 修改用户信息 */
    @POST("edit_user")
    Flowable<UserProfile> updateUserProfile(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 意见反馈 */
    @POST("opinion")
    Flowable<Object> feedback(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 添加设备 */
    @POST("add_device")
    Flowable<Object> addDevice(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 移除设备 */
    @POST("remove_device")
    Flowable<Object> removeDevice(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 重命名设备 */
    @POST("rename_device")
    Flowable<Object> renameDevice(@Header("authorization") String token, @Body HashMap<String, Object> param);

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

    /* 选择区域 */
    @POST("region/get")
    Flowable<List<RegionInfo>> getRegion(@Header("authorization") String token, @Body HashMap<String, Object> param);

    /* 记录数据日志 */
    @POST("device_action_log")
    Flowable<Object> deviceActionLog(@Header("authorization") String token, @Body HashMap<String, Object> param);
}
