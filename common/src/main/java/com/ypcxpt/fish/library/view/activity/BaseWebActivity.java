package com.ypcxpt.fish.library.view.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.widget.TitleBar;

public abstract class BaseWebActivity extends BaseActivity {

    private AgentWeb mAgentWeb;

    private LinearLayout llContainer;

    private TitleBar titleBar;

    /* 是否已还可以从网页继续返回 */
    private boolean isWebPageBackEnabled;

    @Override
    protected int layoutResID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initViews() {
        titleBar = findViewById(R.id.title_bar);
        llContainer = findViewById(R.id.ll_container);
        initWebView();
    }

    private void initWebView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llContainer, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
//                .setWebLayout(new WebLayout(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(getUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            isWebPageBackEnabled = true;
            return true;
        } else {
            isWebPageBackEnabled = false;
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onDestroy();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (titleBar != null) {
                titleBar.setTitle(title);
            }
        }
    };

    /* 子类提供需要加载的网页地址 */
    public abstract String getUrl();

    /* 是否已还可以从网页继续返回 */
    public boolean isWebPageBackEnabled() {
        return isWebPageBackEnabled;
    }

    /* 提供TitleBar对象 */
    public TitleBar getTitleBar() {
        return titleBar;
    }

}
