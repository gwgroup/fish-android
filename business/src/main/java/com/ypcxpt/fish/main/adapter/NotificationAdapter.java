package com.ypcxpt.fish.main.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.NotificationContract;
import com.ypcxpt.fish.main.model.NotificationInfo;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.sonic.BrowserActivity;
import com.ypcxpt.fish.sonic.SonicJavaScriptInterface;

import static com.ypcxpt.fish.sonic.BrowserActivity.MODE_SONIC;

public class NotificationAdapter extends BaseQuickAdapter<NotificationInfo.RowsBean, BaseViewHolder> {
    private NotificationContract.Presenter mPresenter;

    public NotificationAdapter(int layoutResId, NotificationContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationInfo.RowsBean item) {
        String title = StringHelper.nullToDefault(item.getTitle(), "");
        String content = StringHelper.nullToDefault(item.getContent(), "");
        String time = StringHelper.nullToDefault(item.getCreate_time(), "");
        if (item.getRead() == 0) {
            helper.getView(R.id.iv_read).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_read).setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_titleName, title);
        helper.setText(R.id.tv_content, content);
        helper.setText(R.id.tv_time, TimeUtil.getTimeFormatText(time));
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
//            mPresenter.skipCollectionDetail(item);
            String url = "https://smart.reead.net/message/index.html?token=" + AppData.token() + "&id=" + item.getId();
            startBrowserActivity(url, item, MODE_SONIC);
            item.setRead(1);
        });
    }

    private void startBrowserActivity(String webUrl, NotificationInfo.RowsBean dataBean, int mode) {
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra("TITLE", "通知详情");
        intent.putExtra("TITLE_DATA", dataBean.getTitle());
        intent.putExtra("IMAGE_DATA", "");
        intent.putExtra(BrowserActivity.PARAM_URL, webUrl);
        intent.putExtra(BrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        mContext.startActivity(intent);
    }
}
