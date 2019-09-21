package com.ypcxpt.fish.library.ui.widget.popup.slide;

import android.content.Context;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.CustomView;

public class SlidingMenuItemDivider extends CustomView {
    public SlidingMenuItemDivider(Context context) {
        super(context);
    }

    @Override
    protected int getContentResId() {
        return R.layout.sliding_menu_item_divider;
    }
}
