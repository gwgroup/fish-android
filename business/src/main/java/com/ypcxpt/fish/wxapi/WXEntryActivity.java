package com.ypcxpt.fish.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.ypcxpt.fish.BuildConfig;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.ui.widget.PageLoadingView;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.login.Constants;
import com.ypcxpt.fish.login.model.UserProfileVolley;
import com.ypcxpt.fish.login.model.WXAccessTokenEntity;
import com.ypcxpt.fish.login.model.WXBaseRespEntity;
import com.ypcxpt.fish.login.model.WXUserInfo;
import com.ypcxpt.fish.login.model.WechatLoginResult;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description ：
 * project name：CCloud
 * author : Vincent
 * creation date: 2017/6/9 18:13
 *
 * @version 1.0
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private PageLoadingView mLoadingView;
    /**
     * 微信登录相关
     */
    private IWXAPI api;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingView = new PageLoadingView(this);
        Router.inject(this);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX, true);
        //将应用的appid注册到微信
        api.registerApp(Constants.APP_ID_WX);
        Logger.d("WXEntryActivity", "------------------------------------");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                Logger.d("WXEntryActivity", "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.d("WXEntryActivity", "baseReq:" + gson.toJson(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Logger.d("baseResp:--A", gson.toJson(baseResp));
        Logger.d("baseResp--B:", baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        WXBaseRespEntity entity = gson.fromJson(gson.toJson(baseResp), WXBaseRespEntity.class);
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                showLoading();
                result = "发送成功";
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + Constants.APP_ID_WX + "&secret=" + Constants.APP_SECRET_WX
                        + "&code=" + entity.getCode() + "&grant_type=authorization_code";
                RequestQueue mQueue = Volley.newRequestQueue(WXEntryActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.e("WXEntryActivity", "response:" + response);
//                        Toaster.showShort(response);
                        WXAccessTokenEntity accessTokenEntity = gson.fromJson(response, WXAccessTokenEntity.class);
                        if (accessTokenEntity != null) {
                            getUserInfo(accessTokenEntity);
                        } else {
                            Logger.d("WXEntryActivity", "获取失败");
                        }
                    }
                }, error -> {
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("", "");
                        return map;
                    }
                };
                mQueue.add(stringRequest);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Logger.d("WXEntryActivity", "发送取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Logger.d("WXEntryActivity", "发送被拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = "签名错误";
                Logger.d("WXEntryActivity", "签名错误");
                break;
            default:
                result = "发送返回";
                finish();
                break;
        }
//        Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取微信的个人信息
     *
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessTokenEntity.getAccess_token() + "&openid=" + accessTokenEntity.getOpenid();
        RequestQueue mQueue = Volley.newRequestQueue(WXEntryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.e("获取微信的个人信息", response);
                WXUserInfo wxResponse = gson.fromJson(response, WXUserInfo.class);
                Logger.d("WXEntryActivity", "微信登录资料已获取，后续未完成");
                String headUrl = wxResponse.getHeadimgurl();
//                Logger.d("头像Url:", headUrl);
//                App.getShared().putString("headUrl", headUrl);
//                Intent intent = getIntent();
//                intent.putExtra("wxUserInfo", wxResponse);
//                intent.putExtra("openid", wxResponse.getOpenid());
//                intent.putExtra("nickname", wxResponse.getNickname());
//                intent.putExtra("sex", wxResponse.getSex());
//                intent.putExtra("language", wxResponse.getLanguage());
//                intent.putExtra("city", wxResponse.getCity());
//                intent.putExtra("province", wxResponse.getProvince());
//                intent.putExtra("country", wxResponse.getCountry());
//                intent.putExtra("unionid", wxResponse.getUnionid());
//                intent.putExtra("headimgurl", headUrl);
//                WXEntryActivity.this.setResult(0, intent);
//                finish();

                /**
                 * 调用我们自己的微信登录接口判断是否需要绑定手机号
                 */
                wechatLogin(wxResponse);
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("", "");
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                String str = null;
                try {
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }

        };
        mQueue.add(stringRequest);
    }

    private void wechatLogin(WXUserInfo wxResponse) {
        String url = BuildConfig.BASE_URL + "weixin_login";
        RequestQueue mQueue = Volley.newRequestQueue(WXEntryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissLoading();
                Logger.i("微信登录接口", response);
                WechatLoginResult wechatLoginResult = gson.fromJson(response, WechatLoginResult.class);
                if (wechatLoginResult.isSuccess() && wechatLoginResult.getData() != null) {
                    if (wechatLoginResult.getData().getToken() == null || StringUtils.isTrimEmpty(wechatLoginResult.getData().getToken())) {
                        //没有绑定手机号，跳转绑定手机号页面去绑定
                        Router.build(Path.Login.BIND)
                                .withString("openid", wxResponse.getOpenid())
                                .withFinish()
                                .navigation(WXEntryActivity.this);
                    } else {
                        //绑定过手机号，直接获取用户信息进入首页
                        AppData.setToken(wechatLoginResult.getData().getToken());
                        getReeadUser();
                    }
                }
            }
        }, error -> {
//            Toaster.showLong("error-->" + error.getMessage());
            dismissLoading();
            finish();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("openid", wxResponse.getOpenid());
                map.put("nickname", wxResponse.getNickname());
                map.put("sex", wxResponse.getSex() + "");
                map.put("language", wxResponse.getLanguage());
                map.put("city", wxResponse.getCity());
                map.put("province", wxResponse.getProvince());
                map.put("country", wxResponse.getCountry());
                map.put("unionid", wxResponse.getUnionid());
                map.put("headimgurl", wxResponse.getHeadimgurl());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     * 使用微信登录后发现有token说明绑定了手机号
     * 这时候直接拿token获取用户信息然后进入主页
     */
    private void getReeadUser() {
        String url = BuildConfig.BASE_URL + "user";
        RequestQueue mQueue = Volley.newRequestQueue(WXEntryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.i("user", "userJson-->" + response);
                UserProfileVolley userProfileVolley = gson.fromJson(response, UserProfileVolley.class);
                Router.build(Path.Main.MAIN)
                        .withParcelable("userProfile", userProfileVolley.getData())
                        .withFinish()
                        .navigation(WXEntryActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("", "");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 user-token:AEUHY98QIASUDH
                headers.put("authorization", AppData.token());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    private void showLoading() {
        if (mLoadingView != null) {
            mLoadingView.show();
        }
    }
    private void dismissLoading() {
        if (mLoadingView != null && mLoadingView.isShowing()) {
            mLoadingView.dismiss();
        }
    }
}
