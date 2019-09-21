package com.ypcxpt.fish.device.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class DeviceProgressView extends ProgressBar {
    public DeviceProgressView(Context context) {
        super(context);
        initViews(context);
    }

    public DeviceProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public DeviceProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
    }
}
