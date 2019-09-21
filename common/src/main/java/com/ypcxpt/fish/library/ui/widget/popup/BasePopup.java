package com.ypcxpt.fish.library.ui.widget.popup;

import android.content.Context;

import razerdp.basepopup.BasePopupWindow;

public abstract class BasePopup<T extends PopupItem> extends BasePopupWindow {
    protected OnPopupItemClickListener<T> mItemListener;

    public BasePopup(Context context) {
        super(context);
    }

    public BasePopup(Context context, OnPopupItemClickListener<T> listener) {
        super(context);
        setOnItemClickListener(listener);
    }

    public void setOnItemClickListener(OnPopupItemClickListener listener) {
        mItemListener = listener;
    }

}
