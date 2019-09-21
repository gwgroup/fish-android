package com.ypcxpt.fish.main.view.activity;

import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import com.ypcxpt.fish.BuildConfig;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.login.model.UserProfileVolley;
import com.ypcxpt.fish.main.event.OnGetCommentsEvent;
import com.ypcxpt.fish.main.event.OnGetCommentsWithmeEvent;
import com.ypcxpt.fish.main.view.fragment.CommentForMeFragment;
import com.ypcxpt.fish.main.view.fragment.CommentForOtherFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.COMMENT)
public class CommentActivity extends BaseActivity {
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab) SmartTabLayout tab;

    @Autowired(name = "VIEWPAPER_PAGE")
    public String VIEWPAPER_PAGE = "1";

    private UserProfile mUserProfile;

    @Override
    protected int layoutResID() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initData() {
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetCommentsEvent()), 500);
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetCommentsWithmeEvent()), 500);
    }

    @Override
    protected void initViews() {
        initVp();
    }

    private void initVp() {
        FragmentPagerItems fragmentPagerItems = FragmentPagerItems.with(this)
                .add("我的评论", CommentForMeFragment.class)
                .add("@我的", CommentForOtherFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), fragmentPagerItems);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        if ("2".equals(VIEWPAPER_PAGE)) {
            viewPager.setCurrentItem(1);
            getReeadUser();
        }
        tab.setViewPager(viewPager);
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if ("2".equals(VIEWPAPER_PAGE)) {
            Router.build(Path.Main.MAIN)
                    .withParcelable("userProfile", mUserProfile)
                    .withFinish()
                    .navigation(this);
        } else {
            finish();
        }
    }

    private void getReeadUser() {
        String url = BuildConfig.BASE_URL + "user";
        RequestQueue mQueue = Volley.newRequestQueue(CommentActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.i("user", "userJson-->" + response);
                Gson gson = new Gson();
                UserProfileVolley userProfileVolley = gson.fromJson(response, UserProfileVolley.class);
                mUserProfile = userProfileVolley.getData();
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
}
