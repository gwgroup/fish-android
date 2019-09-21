package com.ypcxpt.fish.library.ui.widget.popup.slide;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.widget.popup.BasePopup;
import com.ypcxpt.fish.library.ui.widget.popup.OnPopupItemClickListener;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;

import java.util.List;

import static com.ypcxpt.fish.library.util.AnimationUtils.getHorizontalAnimation;

public class SlidingMenu<T extends PopupItem> extends BasePopup<T> {

    private final LinearLayout mLlContent;

    public SlidingMenu(Context context, OnPopupItemClickListener<T> listener) {
        super(context, listener);
        mLlContent = findViewById(R.id.ll_content);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getHorizontalAnimation(1f, 0f, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getHorizontalAnimation(0f, 1f, 200);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.sliding_menu);
    }

    public void setNewData(List<T> data) {
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

        mLlContent.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            T itemData = data.get(i);
            SlidingMenuItemView itemView = new SlidingMenuItemView(getContext(), itemData);
            itemView.setOnClickListener(v -> {
                mItemListener.onClick(itemData);
                dismiss();
            });

            mLlContent.addView(itemView);
            if (i != data.size() - 1) {
                mLlContent.addView(new SlidingMenuItemDivider(getContext()));
            }
        }
    }

}
