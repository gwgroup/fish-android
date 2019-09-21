package com.ypcxpt.fish.library.ui.widget.popup.actionsheet;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.widget.popup.BasePopup;
import com.ypcxpt.fish.library.ui.widget.popup.OnPopupItemClickListener;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;
import com.ypcxpt.fish.library.util.ViewHelper;

import java.util.List;

import static com.ypcxpt.fish.library.util.ViewHelper.MATCH;
import static com.ypcxpt.fish.library.util.ViewHelper.WRAP;

public class ActionSheet<T extends PopupItem> extends BasePopup<T> {

    private final LinearLayout mLlContent;

    public ActionSheet(Context context, OnPopupItemClickListener<T> listener) {
        super(context, listener);
        mLlContent = findViewById(R.id.ll_content);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 200);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.action_sheet);
    }

    public void setNewData(List<T> data) {
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

        mLlContent.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            T itemData = data.get(i);
            ActionSheetItemView itemView = new ActionSheetItemView(getContext(), itemData);
            if (i == 0) {
                itemView.setBackground(R.drawable.sl_bg_manual_more_item_first);
            } else if (i == data.size() - 1) {
                itemView.setBackground(R.drawable.sl_bg_manual_more_item_last);
            }
            itemView.setOnClickListener(v -> {
                mItemListener.onClick(itemData);
                dismiss();
            });

            mLlContent.addView(itemView);
            if (i != data.size() - 1) {
                mLlContent.addView(new ActionSheetItemDivider(getContext()));
            }
        }

        PopupItem cancelItem = new PopupItem("取消");
        ActionSheetItemView cancelItemView = new ActionSheetItemView(getContext(), cancelItem);
        cancelItemView.setBackground(R.drawable.sl_bg_manual_more_item_cancel);
        cancelItemView.setBold();
        cancelItemView.setOnClickListener(v -> dismiss());
        LinearLayout.LayoutParams params = ViewHelper.newLinearParams(MATCH, WRAP);
        params.bottomMargin = ConvertUtils.dp2px(12);
        params.topMargin = ConvertUtils.dp2px(10);

        mLlContent.addView(cancelItemView, params);
    }

}
