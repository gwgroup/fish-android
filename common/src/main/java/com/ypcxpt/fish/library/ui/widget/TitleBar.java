package com.ypcxpt.fish.library.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.definition.JConsumer;
import com.ypcxpt.fish.library.ui.CustomView;
import com.ypcxpt.fish.library.util.ResourceUtils;

public class TitleBar extends CustomView implements View.OnClickListener {
    private static final String DEFAULT_TITLE = "REEAD";

    private TextView tvTitle;
    private ImageView ivLeft;
    private TextView tvRight;
    private ImageView ivRight;

    private JConsumer<View> mLeftClick;
    private JConsumer<View> mRightClick;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        super.init(attrs);

        String title = null, rightText = null;
        int leftIconResID = 0, rightIconResID = 0;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);
            title = typedArray.getString(R.styleable.TitleBar_title);
            leftIconResID = typedArray.getResourceId(R.styleable.TitleBar_leftIcon, 0);
            rightText = typedArray.getString(R.styleable.TitleBar_rightText);
            rightIconResID = typedArray.getResourceId(R.styleable.TitleBar_rightIcon, 0);
        }

        title = ObjectUtils.isEmpty(title) ? DEFAULT_TITLE : title;
        leftIconResID = leftIconResID == 0 ? R.mipmap.ic_arrow_back : leftIconResID;

        tvTitle = getContent().findViewById(R.id.tv_title);
        ivLeft = getContent().findViewById(R.id.iv_left);
        tvRight = getContent().findViewById(R.id.tv_right);
        ivRight = getContent().findViewById(R.id.iv_right);

        setTitle(title).setLeftIcon(leftIconResID).setRightText(rightText).setRightIcon(rightIconResID);

        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivRight.setOnClickListener(this);

        mLeftClick = view -> {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).onBackPressed();
            }
        };

        fakeStatusBarHanding();
    }

    private void fakeStatusBarHanding() {
        View fakeStatusBar = getContent().findViewById(R.id.fake_status_bar);
        ViewGroup.LayoutParams params = fakeStatusBar.getLayoutParams();
        params.height = BarUtils.getStatusBarHeight();
        fakeStatusBar.setLayoutParams(params);
    }

    @Override
    protected int getContentResId() {
        return R.layout.titlebar;
    }

    /**
     * 标题.
     */
    public TitleBar setTitle(@StringRes int resID) {
        return setTitle(ResourceUtils.getString(resID));
    }

    /**
     * 标题.
     */
    public TitleBar setTitle(String title) {
        if (ObjectUtils.isEmpty(title)) title = "";

        tvTitle.setText(title);
        return this;
    }

    /**
     * 左ICON. 小于0表示隐藏该ICON.
     */
    public TitleBar setLeftIcon(int resID) {
        if (resID <= 0) {
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(resID);
        }
        return this;
    }

    /**
     * 右文本. “空”表示隐藏该文本.
     */
    public TitleBar setRightText(String text) {
        if (ObjectUtils.isEmpty(text)) {
            tvRight.setVisibility(GONE);
        } else {
            tvRight.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.GONE);
            tvRight.setText(text);
        }
        return this;
    }

    /**
     * 右文本.
     */
    public TitleBar setRightText(@StringRes int resID) {
        setRightText(ResourceUtils.getString(resID));
        return this;
    }

    /**
     * 右ICON. 小于0表示隐藏该ICON.
     */
    public TitleBar setRightIcon(int resID) {
        if (resID <= 0) {
            ivRight.setVisibility(View.GONE);
        } else {
            ivRight.setVisibility(View.VISIBLE);
            tvRight.setVisibility(View.GONE);
            ivRight.setImageResource(resID);
        }
        return this;
    }

    /**
     * 右边点击事件.
     */
    public TitleBar setRightClick(JConsumer<View> rightClick) {
        mRightClick = rightClick;
        return this;
    }

    /**
     * 左边点击事件.
     */
    public TitleBar setLeftClick(JConsumer<View> leftClick) {
        mLeftClick = leftClick;
        return this;
    }

    @Override
    public void onClick(View v) {
        int vieId = v.getId();
        if (vieId == R.id.iv_left) {
            if (mLeftClick != null) {
                mLeftClick.accept(v);
            } else {
            }
        } else if (vieId == R.id.iv_right || vieId == R.id.tv_right) {
            if (mRightClick != null) {
                mRightClick.accept(v);
            }
        }
    }
}
