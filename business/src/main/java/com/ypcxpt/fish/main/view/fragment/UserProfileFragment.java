package com.ypcxpt.fish.main.view.fragment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.jpush.OnRefreshNotificationEvent;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.B64PhotoHelper;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.UserProfileContract;
import com.ypcxpt.fish.main.event.OnGetCollectionsEvent;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.event.OnRabbitMQUpdatedEvent;
import com.ypcxpt.fish.main.event.OnRefreshUserEvent;
import com.ypcxpt.fish.main.model.RabbitMQInfo;
import com.ypcxpt.fish.main.presenter.UserProfilePresenter;
import com.ypcxpt.fish.sonic.BrowserActivity;
import com.ypcxpt.fish.sonic.SonicJavaScriptInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.ypcxpt.fish.app.util.DisplayUtils.displayProfileInfo;
import static com.ypcxpt.fish.sonic.BrowserActivity.MODE_SONIC;

public class UserProfileFragment extends BaseFragment implements UserProfileContract.View {
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_lv)
    TextView tv_lv;
    @BindView(R.id.tv_xp)
    TextView tv_xp;
    @BindView(R.id.tv_coin)
    TextView tv_coin;
    @BindView(R.id.pb_xp)
    ProgressBar pb_xp;
    @BindView(R.id.tv_collectionNum)
    TextView tv_collectionNum;
    @BindView(R.id.tv_notificationNum)
    TextView tv_notificationNum;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;

    private UserProfileContract.Presenter mPresenter;

    private UserProfile mUserProfile;

    private int XP_LV1 = 0;
    private int XP_LV2 = 100;
    private int XP_LV3 = 1000;
    private int XP_LV4 = 5000;
    private int XP_LV5 = 10000;
    private int XP_LV6 = 20000;
    private int XP_LV7 = 40000;
    private int XP_LV8 = 80000;
    private int XP_LV9 = 100000;

    ConnectionFactory factory;

    @Override
    protected int layoutResID() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void initData() {
        mPresenter = new UserProfilePresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "chat";
//            String channelName = "聊天消息";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            createNotificationChannel(channelId, channelName, importance);
//        }

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        swipe_refresh_layout.setRefreshing(false);//取消刷新
                        EventBus.getDefault().post(new OnRefreshUserEvent());
//                        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 500);
                    }
                }, 2000);
            }
        });
        //设置刷新时旋转图标的颜色，这是一个可变参数，当设置多个颜色时，旋转一周改变一次颜色。
        swipe_refresh_layout.setColorSchemeResources(R.color.main_color_new, R.color.bg_device_detail_yellow, R.color.bg_device_detail_top);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 配置RabbitMQ
     */
    private void setUpConnectionFactory() {
        factory = new ConnectionFactory();// 声明ConnectionFactory对象
        factory.setHost("device.reead.net");//主机地址：
        factory.setPort(5672);// 端口号
        factory.setUsername("rh");// 用户名
        factory.setPassword("2q018reead");// 密码
        factory.setVirtualHost("rh");
        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
    }

    @OnClick(R.id.iv_edit_profile)
    public void onEditProfile() {
        Router.build(Path.Main.EDIT_PROFILE)
                .withParcelable("mUserProfile", mUserProfile)
                .navigation(getActivity());
    }

    @OnClick(R.id.rl_edit_profile)
    public void onEditProfileNew() {
        Router.build(Path.Main.EDIT_PROFILE)
                .withParcelable("mUserProfile", mUserProfile)
                .navigation(getActivity());
    }

    @OnClick(R.id.rl_feed_back)
    public void onFeedback() {
        Router.build(Path.Main.EDIT_FEEDBACK)
                .navigation(getActivity());
    }

    @OnClick(R.id.rl_device_manager)
    public void onDeviceManager() {
//        mPresenter.getDevices();
        Router.build(Path.Main.DEVICE_MANAGER).navigation(getActivity());
        //通知下一个界面调用设备列表接口
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
    }

    @OnClick(R.id.ll_collection)
    public void onArticleCollection() {
        Router.build(Path.Main.COLLECTION).navigation(getActivity());
        //通知下一个界面调用收藏列表接口
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetCollectionsEvent()), 500);
    }

    @OnClick(R.id.ll_comment)
    public void onComment() {
        Router.build(Path.Main.COMMENT).withString("VIEWPAPER_PAGE", "1").navigation(getActivity());
    }

    @OnClick(R.id.ll_notification)
    public void onNotification() {
        Router.build(Path.Main.IO_CONFIG).navigation(getActivity());
    }

    int count = 1;

    /**
     * 收到消息后显示
     *
     * @param title   通知标题
     * @param id      详情id
     * @param content 消息内容
     */
    public void showRabbitMqMsg(String title, String id, String content) {
        Logger.e("显示", "title=" + title + ",id=" + id + ",content=" + content);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("chat");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, "com.ypcxpt.fish");
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toaster.showShort("请手动将通知打开");
            }
        }
        Notification notification = new NotificationCompat.Builder(getActivity(), "chat")
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_detected_device_new)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(getContentIntent(id, title, content))
                .build();
        manager.notify(count, notification);
        count++;
    }

    public PendingIntent getContentIntent(String id, String title, String content) {
        String webUrl = "http://47.96.146.0:9002/article/index.html?token=" + AppData.token() + "&id=" + id;
        Intent intent = new Intent(getActivity(), BrowserActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("TITLE_DATA", content);
        intent.putExtra("IMAGE_DATA", "");
        intent.putExtra(BrowserActivity.PARAM_URL, webUrl);
        intent.putExtra(BrowserActivity.PARAM_MODE, MODE_SONIC);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        return PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 收消息
     */
    private void basicConsume() {

        try {
            //连接
            Connection connection = factory.newConnection();
            //通道
            final Channel channel = connection.createChannel();
            //声明了一个交换和一个服务器命名的队列，然后将它们绑定在一起。
//            channel.exchangeDeclare("amq.topic" , "topic" , true);
            String queueName = "rh_" + mUserProfile.user.phoneNo;
            Logger.e("TAG", queueName + " :queueName");
            channel.queueDeclare(queueName, false, false, true, null);
            channel.queueBind(queueName, "amq.topic", "/rh/all");
            channel.queueBind(queueName, "amq.topic", "/rh/" + mUserProfile.user.phoneNo);
            //实现Consumer的最简单方法是将便捷类DefaultConsumer子类化。可以在basicConsume 调用上传递此子类的对象以设置订阅：
            channel.basicConsume(queueName, false, "android", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);

                    String rountingKey = envelope.getRoutingKey();//路由密钥
                    String contentType = properties.getContentType();//contentType字段，如果尚未设置字段，则为null。
                    String msg = new String(body, "UTF-8");//接收到的消息
                    long deliveryTag = envelope.getDeliveryTag();//交付标记

                    Logger.e("TAG", rountingKey + "：rountingKey");
                    Logger.e("TAG", contentType + "：contentType");
                    Logger.e("TAG", msg + "");
                    Logger.e("TAG", deliveryTag + "：deliveryTag");
                    Logger.e("TAG", consumerTag + "：consumerTag");
                    Logger.e("TAG", envelope.getExchange() + "：exchangeName");

                    Gson gson = new Gson();
                    RabbitMQInfo rabbitMQInfo = gson.fromJson(msg, RabbitMQInfo.class);
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnRabbitMQUpdatedEvent(rabbitMQInfo)), 500);

                    channel.basicAck(deliveryTag, false);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.tv_logout)
    public void onClickLogout() {
        mPresenter.logout(AppData.token());
    }

    @OnClick(R.id.rl_logout)
    public void onClickLogoutNew() {
        mPresenter.logout(AppData.token());
    }

    private void displayGender(UserProfile userProfile) {
//        String gender = userProfile.getGender();
        displayProfileInfo(tvGender, "");
        /* 如果没有设置头像，则默认头像随性别改变 */
        if (StringUtils.isTrimEmpty(userProfile.user.avatar)) {
            ivAvatar.setImageResource(R.mipmap.ic_default_avatar_male);
        }
    }

    private void displayAvatar(UserProfile userProfile) {
        String avatar = userProfile.user.avatar;
        /* 如果没有设置头像，则不处理，上面已经处理过 */
        if (StringUtils.isTrimEmpty(avatar)) {
            return;
        }
        B64PhotoHelper.load(getActivity(), avatar, ivAvatar);
    }

    public void onProfileRefresh(UserProfile userProfile) {
        if (userProfile == null) {
            return;
        }

        mUserProfile = userProfile;
        displayProfileInfo(tvNickName, userProfile.user.display_name);
//        displayProfileInfo(tvBirthday, getFormatDate(userProfile.birthday));
//        displayProfileInfo(tvPhoneNo, getFormatPhone(userProfile.phoneNo));
//        displayProfileInfo(tvAddress, userProfile.address);
//        String strWeight = userProfile.weight > 0 ? String.format("%dkg", userProfile.weight) : null;
//        displayProfileInfo(tvWeight, strWeight);
//        String strHeight = userProfile.height > 0 ? String.format("%dcm", userProfile.height) : null;
//        displayProfileInfo(tvHeight, strHeight);
        displayGender(userProfile);
        displayAvatar(userProfile);

//        displayIntInfo(tv_coin, userProfile.integral);
//        displayXP(userProfile.xp);
//        displayCollectionInfo(tv_collectionNum, userProfile.star_count);
//        displayNotificationInfo(tv_notificationNum, userProfile.unread_notice_count);
    }

    private void displayNotificationInfo(TextView tv, String info) {
        if (!StringUtils.isTrimEmpty(info)) {
            if (info.contains(".")) {
                String notificationNum = info.substring(0, info.indexOf("."));
                tv.setText(notificationNum);

                if (StringUtils.isTrimEmpty(notificationNum) || "0".equals(notificationNum)) {
                    iv_notification.setVisibility(View.GONE);
                } else {
                    iv_notification.setVisibility(View.VISIBLE);
                }
            } else {
                tv.setText(info);
                if (StringUtils.isTrimEmpty(info) || "0".equals(info)) {
                    iv_notification.setVisibility(View.GONE);
                } else {
                    iv_notification.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void displayCollectionInfo(TextView tv, int info) {
        if (info > 0) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(info + "");
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void displayIntInfo(TextView tv, String info) {
        if (info.contains(".")) {
            tv.setText(info.substring(0, info.indexOf(".")));
        } else {
            tv.setText(info);
        }
    }

    private void displayXP(int xp) {
        int userXP = xp;
        if (userXP >= XP_LV1 && userXP < XP_LV2) {
            tv_lv.setText("  LV1");
            showXPprogress(userXP, XP_LV2);
        } else if (userXP >= XP_LV2 && userXP < XP_LV3) {
            tv_lv.setText("  LV2");
            showXPprogress(userXP, XP_LV3);
        } else if (userXP >= XP_LV3 && userXP < XP_LV4) {
            tv_lv.setText("  LV3");
            showXPprogress(userXP, XP_LV4);
        } else if (userXP >= XP_LV4 && userXP < XP_LV5) {
            tv_lv.setText("  LV4");
            showXPprogress(userXP, XP_LV5);
        } else if (userXP >= XP_LV5 && userXP < XP_LV6) {
            tv_lv.setText("  LV5");
            showXPprogress(userXP, XP_LV6);
        } else if (userXP >= XP_LV6 && userXP < XP_LV7) {
            tv_lv.setText("  LV6");
            showXPprogress(userXP, XP_LV7);
        } else if (userXP >= XP_LV7 && userXP < XP_LV8) {
            tv_lv.setText("  LV7");
            showXPprogress(userXP, XP_LV8);
        } else if (userXP >= XP_LV8 && userXP < XP_LV9) {
            tv_lv.setText("  LV8");
            showXPprogress(userXP, XP_LV9);
        } else if (userXP >= XP_LV9) {
            tv_lv.setText("  LV9");
            showXPprogress(XP_LV9, XP_LV9);
        }
    }

    private void showXPprogress(int xp, int maxXP) {
        tv_xp.setText("经验值" + xp + "/" + maxXP);
        pb_xp.setMax(maxXP);
        pb_xp.setProgress(xp);
    }

    @Subscribe
    public void onEventReceived(OnProfileUpdatedEvent event) {
        SPHelper.putString("USER_ID", event.userProfile.user.id);
        mPresenter.refreshDevice(event.userProfile);
        onProfileRefresh(event.userProfile);

//        setUpConnectionFactory();
//        new Thread(() -> basicConsume()).start();
    }

    @Subscribe
    public void onEventRabbitMqReceived(OnRabbitMQUpdatedEvent event) {
        Logger.e("收到消息", "onEventRabbitMqReceived");
        showRabbitMqMsg("收到一条新消息", event.rabbitMQInfo.getData().getId(), event.rabbitMQInfo.getData().getTitle());
    }

    //下拉刷新User信息
    @Subscribe
    public void onEventReceived(OnRefreshUserEvent event) {
        mPresenter.refreshUser();
    }

    //收到极光推送通知
    @Subscribe
    public void onEventReceived(OnRefreshNotificationEvent event) {
        iv_notification.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterLogoutSuccess() {
        Router.build(Path.Login.LOGIN).withFinish().navigation(getActivity());
    }
}
