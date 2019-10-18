package com.ypcxpt.fish.main.adapter;

import android.content.Intent;
import android.util.Base64;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.CommentWithMeContract;
import com.ypcxpt.fish.main.model.CommentInfo;
import com.ypcxpt.fish.app.util.FileUtil;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.sonic.BrowserActivity;
import com.ypcxpt.fish.sonic.SonicJavaScriptInterface;

import static com.ypcxpt.fish.sonic.BrowserActivity.MODE_SONIC;

public class CommentWithMeAdapter extends BaseQuickAdapter<CommentInfo.RowsBean, BaseViewHolder> {
    private CommentWithMeContract.Presenter mPresenter;
    private String shareImageUrl = "";

    public CommentWithMeAdapter(int layoutResId, CommentWithMeContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentInfo.RowsBean item) {
        helper.setIsRecyclable(false);
        String myName = StringHelper.nullToDefault(item.getUser_name(), "");
        String myContent = StringHelper.nullToDefault(item.getContent(), "");
        String otherName = StringHelper.nullToDefault(item.getTo_user_name(), "");
        String otherContent = StringHelper.nullToDefault(item.getP_content(), "");
        String myCommentTime = StringHelper.nullToDefault(item.getCreate_time(), "");
        String otherCommentTime = StringHelper.nullToDefault(item.getP_create_time(), "");
        String articleTitle = StringHelper.nullToDefault(item.getArticle_title(), "");
        String articleTime = StringHelper.nullToDefault(item.getArticle_create_time(), "");

        String avatar = item.getIcon();
        /* 如果没有设置头像，则不处理，上面已经处理过 */
        if (StringUtils.isTrimEmpty(avatar)) {
            return;
        }
        if (avatar.contains(",")) {
            avatar = avatar.split(",")[1];
        }
        byte[] imageBytes = Base64.decode(avatar, Base64.DEFAULT);
        ImageView ivAvatar = helper.getView(R.id.iv_avatarOther);
        Glide.with(mContext)
                .asBitmap()
                .load(imageBytes)
                .into(ivAvatar);

        helper.setText(R.id.tv_otherName, myName);
        helper.setText(R.id.tv_myName, "你");
        helper.setText(R.id.tv_otherComment, myContent);
        helper.setText(R.id.tv_otherCommentTime, TimeUtil.getTimeFormatText(myCommentTime));

        helper.setText(R.id.tv_myName2, "你的评论");
        helper.setText(R.id.tv_myComment, otherContent);
        helper.setText(R.id.tv_myCommentTime, TimeUtil.getTimeFormatText(otherCommentTime));

        Gson gson = new Gson();
        ImageView imageView = helper.getView(R.id.iv_article);
        if (!StringUtils.isTrimEmpty(item.getArticle_small_image())) {
            CommentInfo.ImageBean imageBean = gson.fromJson(item.getArticle_small_image(), CommentInfo.ImageBean.class);
            Glide.with(mContext)
                    .load(imageBean.getUrl())
                    .into(imageView);
            shareImageUrl = imageBean.getUrl();
        } else {
            Glide.with(mContext)
                    .load(R.mipmap.icon_login_logo)
                    .into(imageView);
        }
        helper.setText(R.id.tv_titleName, articleTitle);
        helper.setText(R.id.tv_time, FileUtil.exchangeTime(articleTime));

        helper.getView(R.id.rl_item).setOnClickListener(v -> {
//            mPresenter.skipCollectionDetail(item);
            String url = "https://smart.reead.net/article/index.html?token=" + AppData.token() + "&id=" + item.getArticle_id();
            if (FileUtil.isFastClick()) {
                // 进行点击事件后的逻辑操作，防止连续点击
                startBrowserActivity(url, item, MODE_SONIC);
            }
        });
    }

    private void startBrowserActivity(String webUrl, CommentInfo.RowsBean dataBean, int mode) {
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra("TITLE", "文章详情");
        intent.putExtra("ID_DATA", dataBean.getArticle_id());
        intent.putExtra("TITLE_DATA", dataBean.getArticle_title());
        intent.putExtra("IMAGE_DATA", shareImageUrl);
        intent.putExtra(BrowserActivity.PARAM_URL, webUrl);
        intent.putExtra(BrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        mContext.startActivity(intent);
    }
}
