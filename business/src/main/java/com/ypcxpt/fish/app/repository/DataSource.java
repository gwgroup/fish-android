package com.ypcxpt.fish.app.repository;

import com.ypcxpt.fish.device.model.DataHistory;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.login.model.LoginResult;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.main.model.VersionDetailInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.List;

import io.reactivex.Flowable;

public interface DataSource {
    /* 发送验证码. */
    Flowable<Object> getVerifyCode(String phoneNo);

    /* 登录 */
    Flowable<LoginResult> login(String phoneNo, String verifyCode);

    /* 获取用户信息 */
    Flowable<UserProfile> getUserProfile();

    /* 获取场景 */
    Flowable<List<Scenes>> getScenes();

    /* 获取设备IO信息 */
    Flowable<List<IoInfo>> getIoInfo(String mac);

    /* 打开设备IO. */
    Flowable<Object> openIO(String mac, String code, int duration);

    /* 关闭设备IO. */
    Flowable<Object> closeIO(String mac, String code);

    /* 添加场景 */
    Flowable<Object> addScenes(String mac, String name);

    /* 移除场景 */
    Flowable<Object> removeScenes(String mac);

    /* 重命名场景 */
    Flowable<Object> renameScenes(String mac, String name);


    /* 绑定手机号 */
    Flowable<LoginResult> bindPhone(String phoneNo, String verifyCode, String openid);

    /* 修改用户信息 */
    Flowable<UserProfile> updateUserProfile(UserProfile userProfile);

    /* 意见反馈. */
    Flowable<Object> feedback(String content);

    /* 查询天气 */
    Flowable<WeatherInfo> getWeatherInfo(double lat, double lng);

    /* 查询版本 */
    Flowable<VersionDetailInfo> getVersion(String versionCode);

    /* 获取收藏的文章列表 */
    Flowable<CollectionInfo> getCollections(String pageIndex, String pageNum);

    /* 获取通知列表 */
    Flowable<NotificationInfo> getNotifications(String pageIndex, String pageNum);

    /* 获取我的评论列表 */
    Flowable<CommentInfo> getMyComments(String pageIndex, String pageNum);

    /* 获取@我的评论列表 */
    Flowable<CommentInfo> getCommentsWithMe(String pageIndex, String pageNum);

    /* 使用设备记录一下 */
    Flowable<Object> getControlDevice(String mac);

    /* 记录数据日志 */
    Flowable<Object> deviceActionLog(List<DataHistory> historyList);
}
