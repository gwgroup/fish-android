package com.ypcxpt.fish.library.util;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ViewHelper {

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;

    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int CLICK_INTERVAL = 1000;

    public static ViewGroup.LayoutParams newParams(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }

    public static FrameLayout.LayoutParams newFrameParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams newLinearParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static RelativeLayout.LayoutParams newRelativeParams(int width, int height) {
        return new RelativeLayout.LayoutParams(width, height);
    }

    /**
     * Debounce click.
     *
     * @param view
     * @param consumer
     */
    public static void onClick(View view, Consumer<Object> consumer) {
        RxView.clicks(view)
                .throttleFirst(CLICK_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    public static String getText(TextView tv) {
        return StringHelper.nullToEmpty(tv.getText().toString().trim());
    }

    public static String getTextWithNull(TextView tv) {
        String text = getText(tv);
        return StringUtils.isTrimEmpty(text) ? null : text;
    }

    public static boolean isEmptyText(TextView tv) {
        return ObjectUtils.isEmpty(getText(tv));
    }

    /**
     * 设置字体颜色
     */
    public static void setTextColor(TextView tv, @ColorRes int colorResId) {
        if (tv == null) return;
        tv.setTextColor(ResourceUtils.getColor(colorResId));
    }

    /**
     * 设置文字内容，如果text为空，默认赋值空字符串.
     */
    public static void setText(TextView tv, String text) {
        if (tv == null) return;
        text = StringHelper.nullToEmpty(text);
        tv.setText(text);
    }

    /**
     * 设置文字内容，如果text为空，则不赋值.
     */
    public static void setNoneNullText(TextView tv, String text) {
        if (tv == null || text == null) return;
        tv.setText(text);
    }

    /**
     * 设置左图标.
     */
    public static void setDrawableRight(TextView tv, @DrawableRes int resID) {
        if (tv == null) return;
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resID, 0);
    }

    /**
     * 光标移动到最后一个字符.
     */
    public static void moveToLastCursor(EditText et) {
        String text = ViewHelper.getText(et);
        if (!ObjectUtils.isEmpty(text)) {
            et.setSelection(text.length());
        }
    }

    /**
     * RecyclerView的内容是否铺满当前屏幕.
     */
    public static boolean isItemsFillScreen(RecyclerView rv) {
        if (rv == null) return false;

        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstCompletelyVisibleItemPosition =
                    ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            /* 第一可见的item的index大于0，说明铺满当前屏幕 */
            return firstCompletelyVisibleItemPosition > 0;
        }

        return false;
    }
}
