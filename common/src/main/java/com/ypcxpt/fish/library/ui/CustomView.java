package com.ypcxpt.fish.library.ui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

public abstract class CustomView extends FrameLayout {
    private ViewGroup viewContent;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @CallSuper
    protected void init() {
        viewContent = (ViewGroup) View.inflate(getContext(), getContentResId(), this);
        ButterKnife.bind(this, viewContent);
    }

    @CallSuper
    protected void init(@Nullable AttributeSet attrs) {
        init();
    }

    @CallSuper
    private void init(AttributeSet attrs, int defStyleAttr) {
        init();
    }

    @LayoutRes
    protected abstract int getContentResId();

    protected ViewGroup getContent() {
        return viewContent;
    }

}
