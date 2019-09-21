package com.ypcxpt.fish.library.ui.widget.popup;

import android.content.Context;
import android.widget.TextView;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.ui.CustomView;

public abstract class BaseItemView<T extends PopupItem> extends CustomView {
    protected T mItemData;

    protected TextView tvName;

    public BaseItemView(Context context, T itemData) {
        super(context);
        mItemData = itemData;
        afterInit();
    }

    private void afterInit() {
        tvName = findViewById(R.id.tv_name);
        tvName.setText(mItemData.name);
    }
}
