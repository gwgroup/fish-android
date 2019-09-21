package com.ypcxpt.fish.app.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import com.ypcxpt.fish.BuildConfig;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.adapter.RegionAdapter;
import com.ypcxpt.fish.main.model.RegionInfo;
import com.ypcxpt.fish.main.model.RegionInfoVolley;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @name 省市区四级联动
 * @class name：com.ypcxpt.fish.app.util
 * @class describe
 * @anthor Lenny
 * @time 2019/3/12 15:47
 * @change
 * @chang time
 * @class describe
 */

public class DialogRegion {
    private static Dialog bottomDialog;
    private static TextView tv_regionName;
    private static String address = "";
    private static RecyclerView rv_01;
    private static RegionAdapter mAdapter01;
    private static RecyclerView rv_02;
    private static RegionAdapter mAdapter02;

    public static void showRegionDialog(Activity activity, final List<RegionInfo> regionInfos, final RegionListener listener) {
        bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_region, null, false);
        tv_regionName = contentView.findViewById(R.id.tv_regionName);
        rv_01 = contentView.findViewById(R.id.rv_01);
        rv_02 = contentView.findViewById(R.id.rv_02);
        mAdapter01 = new RegionAdapter(R.layout.item_region, activity);
        rv_01.setLayoutManager(new LinearLayoutManager(activity));
        rv_01.setAdapter(mAdapter01);
        mAdapter01.setNewData(regionInfos);
        mAdapter01.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                address = "";
                address += "" + regionInfos.get(position).getName();
                tv_regionName.append(" " + regionInfos.get(position).getName());

                getRegionnext(activity, regionInfos.get(position).getCode(), listener);
            }
        });


        TextView cancel = contentView.findViewById(R.id.dlg_shared_cancel);
        cancel.setOnClickListener(view -> bottomDialog.dismiss());

        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    public interface RegionListener {
        void searchComplete(String code, String address);
    }

    private static void getRegionnext(Activity activity, String code, RegionListener listener) {
        String url = BuildConfig.BASE_URL + "region/get";
        RequestQueue mQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.i("region", "regionJson-->" + response);
                rv_01.setVisibility(View.GONE);
                rv_02.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                RegionInfoVolley regionInfoVolley = gson.fromJson(response, RegionInfoVolley.class);

                mAdapter02 = new RegionAdapter(R.layout.item_region, activity);
                rv_02.setLayoutManager(new LinearLayoutManager(activity));
                rv_02.setAdapter(mAdapter02);
                mAdapter02.setNewData(regionInfoVolley.getData());
                mAdapter02.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        address += "" + regionInfoVolley.getData().get(position).getName();
                        tv_regionName.append("-" + regionInfoVolley.getData().get(position).getName());

                        if (regionInfoVolley.getData().get(position).getCode().length() == 9) {
                            bottomDialog.dismiss();
                            listener.searchComplete(regionInfoVolley.getData().get(position).getCode(), address);
                        } else {
                            getRegionnext(activity, regionInfoVolley.getData().get(position).getCode(), listener);
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("parent_code", code);
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
