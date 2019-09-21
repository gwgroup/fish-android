package com.ypcxpt.fish.library.ui.widget.popup.slide;

import android.content.Context;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.widget.popup.BaseItemView;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;

public class SlidingMenuItemView<T extends PopupItem> extends BaseItemView<T> {

    public SlidingMenuItemView(Context context, T itemData) {
        super(context, itemData);
    }

    @Override
    protected int getContentResId() {
        return R.layout.sliding_menu_item;
    }

}
