package com.ypcxpt.fish.library.ui.widget.popup.actionsheet;

import android.content.Context;
import android.graphics.Typeface;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.widget.popup.BaseItemView;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;

public class ActionSheetItemView<T extends PopupItem> extends BaseItemView<T> {

    public ActionSheetItemView(Context context, T itemData) {
        super(context, itemData);
    }

    @Override
    protected int getContentResId() {
        return R.layout.action_sheet_item;
    }


    public void setBackground(int resID) {
        tvName.setBackgroundResource(resID);
    }

    public void setBold() {
        tvName.setTypeface(Typeface.DEFAULT_BOLD);
    }

}
