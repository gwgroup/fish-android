package com.ypcxpt.fish.library.ui.widget.popup.actionsheet;

import android.content.Context;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.CustomView;

public class ActionSheetItemDivider extends CustomView {
    public ActionSheetItemDivider(Context context) {
        super(context);
    }

    @Override
    protected int getContentResId() {
        return R.layout.action_sheet_item_divider;
    }
}
