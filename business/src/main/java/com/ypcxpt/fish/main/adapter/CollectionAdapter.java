package com.ypcxpt.fish.main.adapter;

import android.content.Intent;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.CollectionContract;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.app.util.FileUtil;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.sonic.BrowserActivity;
import com.ypcxpt.fish.sonic.SonicJavaScriptInterface;

import static com.ypcxpt.fish.sonic.BrowserActivity.MODE_SONIC;

public class CollectionAdapter extends BaseQuickAdapter<CollectionInfo.RowsBean, BaseViewHolder> {
    private CollectionContract.Presenter mPresenter;
    private String shareImageUrl = "";

    public CollectionAdapter(int layoutResId, CollectionContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionInfo.RowsBean item) {
        String title = StringHelper.nullToDefault(item.getTitle(), "");
        String time = StringHelper.nullToDefault(item.getCreate_time(), "");

        Gson gson = new Gson();
        ImageView imageView = helper.getView(R.id.iv_article);
        if (!StringUtils.isTrimEmpty(item.getSmall_image())) {
            CollectionInfo.ImageBean imageBean = gson.fromJson(item.getSmall_image(), CollectionInfo.ImageBean.class);
            Glide.with(mContext)
                    .load(imageBean.getUrl())
                    .into(imageView);
            shareImageUrl = imageBean.getUrl();
        } else {
            Glide.with(mContext)
                    .load(R.mipmap.ic_company_logo)
                    .into(imageView);
        }

        helper.setText(R.id.tv_titleName, title);
        helper.setText(R.id.tv_time, "发布于" + TimeUtil.getTimeFormatText(time));
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
//            mPresenter.skipCollectionDetail(item);
            String url = "https://smart.reead.net/article/index.html?token=" + AppData.token() + "&id=" + item.getId();
            if (FileUtil.isFastClick()) {
                // 进行点击事件后的逻辑操作，防止连续点击
                startBrowserActivity(url, item, MODE_SONIC);
            }
        });
    }

    private void startBrowserActivity(String webUrl, CollectionInfo.RowsBean dataBean, int mode) {
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra("TITLE", "文章详情");
        intent.putExtra("ID_DATA", dataBean.getId());
        intent.putExtra("TITLE_DATA", dataBean.getTitle());
        intent.putExtra("IMAGE_DATA", shareImageUrl);
        intent.putExtra(BrowserActivity.PARAM_URL, webUrl);
        intent.putExtra(BrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        mContext.startActivity(intent);
    }
}
