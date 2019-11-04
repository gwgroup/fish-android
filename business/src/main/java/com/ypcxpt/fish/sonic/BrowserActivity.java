/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.ypcxpt.fish.sonic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.DialogUtils;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.login.Constants;
import com.ypcxpt.fish.main.event.OnRefreshUserEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A demo browser activity
 * In this demo there are three modes,
 * sonic mode: sonic mode means webview loads html by sonic,
 * offline mode: offline mode means webview loads html from local offline packages,
 * default mode: default mode means webview loads html in the normal way.
 */

public class BrowserActivity extends Activity {
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SONIC = 1;
    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;

    public final static String PARAM_URL = "param_url";
    public final static String PARAM_MODE = "param_mode";

    private SonicSession sonicSession;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(BrowserActivity.this, Constants.APP_ID_WX, true);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID_WX);

        Intent intent = getIntent();
        String url = intent.getStringExtra(PARAM_URL);
        int mode = intent.getIntExtra(PARAM_MODE, -1);
        if (TextUtils.isEmpty(url) || -1 == mode) {
            finish();
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }

        SonicSessionClientImpl sonicSessionClient = null;

        // if it's sonic mode , startup sonic session at first time
        if (MODE_DEFAULT != mode) { // sonic mode
            SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
            sessionConfigBuilder.setSupportLocalServer(true);

            // if it's offline pkg mode, we need to intercept the session connection
            if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
                sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
                    @Override
                    public String getCacheData(SonicSession session) {
                        return null; // offline pkg does not need cache
                    }
                });

                sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
                    @Override
                    public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                        return new OfflinePkgSessionConnection(BrowserActivity.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
            if (null != sonicSession) {
                sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
//                Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
            }
        }

        // start init flow ...
        // in the real world, the init flow may cost a long time as startup
        // runtime、init configs....
        setContentView(R.layout.activity_browser);

        RelativeLayout rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(v -> onBackPressed());
        TextView tv_title = findViewById(R.id.tv_title);
        //导航栏标题
        String TITLE = intent.getStringExtra("TITLE");
        tv_title.setText(TITLE);
        //图片url
        String imageUrl = intent.getStringExtra("IMAGE_DATA");
        RelativeLayout rl_share = findViewById(R.id.rl_share);
        if ("通知详情".equals(TITLE)) {
            rl_share.setVisibility(View.GONE);
        } else {
            rl_share.setVisibility(View.VISIBLE);
        }
        //文章id
        String id = intent.getStringExtra("ID_DATA");
        //文章title
        String articleTitle = intent.getStringExtra("TITLE_DATA");
        //如果文章分享到微信则需要用以下链接
        String wechatUrl = "https://smart.reead.net/transpond/index.html?id=" + id;
        rl_share.setOnClickListener(v -> {
            DialogUtils.showSharedDialog(BrowserActivity.this, wechatUrl, new DialogUtils.SharedListener() {
                @Override
                public void sharedToWXFriend(String content) {
                    Logger.i("微信分享小图片&网址-->", imageUrl + "," + wechatUrl);
                    if (!StringUtils.isTrimEmpty(imageUrl)) {
                        getImage(imageUrl, new HttpCallBackListener() {
                            @Override
                            public void onFinish(Bitmap bitmap) {
                                runOnUiThread(() -> sendToWeiXin(articleTitle, content, "小鱼", bitmap, 0));
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    } else {
                        sendToWeiXin(articleTitle, content, "小鱼", BitmapFactory.decodeResource(getResources(), R.mipmap.icon_login_logo), 1);
                    }

                }

                @Override
                public void sharedToWXFriendCircle(String content) {
                    if (!StringUtils.isTrimEmpty(imageUrl)) {
                        getImage(imageUrl, new HttpCallBackListener() {
                            @Override
                            public void onFinish(Bitmap bitmap) {
                                runOnUiThread(() -> sendToWeiXin(articleTitle, content, "小鱼", bitmap, 1));
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    } else {
                        sendToWeiXin(articleTitle, content, "小鱼", BitmapFactory.decodeResource(getResources(), R.mipmap.icon_login_logo), 1);
                    }
                }

                @Override
                public void sharedToWXCollect(String content) {
                    if (!StringUtils.isTrimEmpty(imageUrl)) {
                        getImage(imageUrl, new HttpCallBackListener() {
                            @Override
                            public void onFinish(Bitmap bitmap) {
                                runOnUiThread(() -> sendToWeiXin(articleTitle, content, "小鱼", bitmap, 2));
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    } else {
                        sendToWeiXin(articleTitle, content, "小鱼", BitmapFactory.decodeResource(getResources(), R.mipmap.icon_login_logo), 2);
                    }
                }
            });
        });

        // init webview
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }
        });

        WebSettings webSettings = webView.getSettings();

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.setJavaScriptEnabled(true);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
        webView.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView);
            sonicSessionClient.clientReady();
        } else { // default mode
            webView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new OnRefreshUserEvent());
        ThreadHelper.postDelayed(() -> finish(), 200);
//        super.onBackPressed();
//        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnRefreshUserEvent()), 500);
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        super.onDestroy();
    }


    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        protected String internalGetCustomHeadFieldEtag() {
            return SonicSessionConnection.CUSTOM_HEAD_FILED_ETAG;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }

    /**
     * @param title       分享的标题
     * @param openUrl     点击分享item打开的网页地址url
     * @param description 网页的描述
     * @param icon        分享item的图片
     * @param requestCode 0表示为分享到微信好友  1表示为分享到朋友圈 2表示微信收藏
     */
    public void sendToWeiXin(String title, String openUrl, String description, Bitmap icon, int requestCode) {
        //初始化一个WXWebpageObject对象，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = openUrl;
        //Y用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题、描述
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;//网页标题
        msg.description = description;//网页描述
        msg.setThumbImage(icon);
//        msg.thumbData =FileUtil.getHtmlByteArray(icon, true);
        //构建一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "supplier";
        req.message = msg;
        req.scene = requestCode;
        api.sendReq(req);
    }

    /**
     * bitmap转换
     *
     * @param
     * @return
     */
    public void getImage(final String path, final HttpCallBackListener listener) {
        new Thread(() -> {
            URL imageUrl = null;
            try {
                imageUrl = new URL(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap bitmap1 = createBitmapThumbnail(bitmap, false);
                if (listener != null) {
                    listener.onFinish(bitmap1);
                }
                is.close();
            } catch (IOException e) {
                if (listener != null) {
                    listener.onError(e);
                }
                e.printStackTrace();
            }
        }).start();
    }

    public Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycler) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 80;
        int newHeight = 80;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (needRecycler) bitmap.recycle();
        return newBitMap;
    }

    //自定义一个接口
    public interface HttpCallBackListener {
        void onFinish(Bitmap bitmap);

        void onError(Exception e);
    }
}
