package com.ypcxpt.fish.library.ui.fuction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ypcxpt.fish.library.R;

public class LongClickDeviceItemView extends RelativeLayout {
    private OnActionListener mOnActionListener;

    public LongClickDeviceItemView(Context context) {
        super(context);
        initViews(context);
    }

    public LongClickDeviceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public LongClickDeviceItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void setOnActionListener(OnActionListener listener) {
        mOnActionListener = listener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        setOnTouchListener((v, event) -> false);
        super.setOnClickListener(l);
    }

    private void initViews(Context context) {
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mOnActionListener != null) {
                        mOnActionListener.onActionDown(this);
                    }
                    setBackgroundResource(R.drawable.bg_device_detail_function_new_pressed);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mOnActionListener != null) {
                        mOnActionListener.onActionUp(this);
                    }
                    setBackgroundResource(R.drawable.bg_device_detail_function_new);
                    break;
            }
            return true;
        });
    }

}
