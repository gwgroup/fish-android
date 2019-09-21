package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypcxpt.fish.R;

/**
 * Created by win7 on 2017-04-24.
 */

public class VersionUpdateDialog extends Dialog implements View.OnClickListener {

    private Button bt_cancel;
    private Button bt_ok;
    private TextView tv_desc;
    private TextView tv_title;
    private TextView tv_versioncode;
    private ListView rv_content;
    private LinearLayout layout_content;
    private View view_line;
    private boolean isCenter = true;
    private LinearLayout ck;
    private Context context;

    private ProgressBar progressBar;

    private OnResultListener mListener;// 回调

    public VersionUpdateDialog(Context context) {
        super(context);
        init();
    }

    public VersionUpdateDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public VersionUpdateDialog(Context context, int theme, boolean isCenter) {
        super(context, theme);
        this.isCenter = isCenter;
        init();
    }

    public VersionUpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
        if (rv_content.getAdapter() != null && ((VersionUpdateAdapter) (rv_content.getAdapter())).getRowCount() > 10)//这里是更新内容介绍 没有请屏蔽
        {
            layout_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(getContext(), 300)));
        }

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setDesccontent(String str) {
        tv_desc.setText(str + ": ");
    }

    public void setPercent(int Percent) {
        progressBar.setProgress(Percent);
        setButtonOk("下载中(" + Percent + "%)..");
    }

    /**
     * 初始化数据
     */
    private void init() {
        setContentView(R.layout.dialog_versionupdate);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        tv_desc = (TextView) findViewById(R.id.tv_version_desc);
        ck = (LinearLayout) findViewById(R.id.ll_version_ignore);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_versioncode = (TextView) findViewById(R.id.tv_version_code);
        rv_content = (ListView) findViewById(R.id.rv_content);
        layout_content = (LinearLayout) findViewById(R.id.layout_version_content);
        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck.setSelected(!ck.isSelected());
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mListener.Cancel(ck.isSelected());
            }
        });
    }

    public void setVersionCode(String code) {
        tv_versioncode.setText(code);
    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void Ok();

        void Cancel(boolean isignore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                mListener.Ok();
                break;
            case R.id.bt_cancel:
                mListener.Cancel(ck.isSelected());
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 设置内容
     */
    public void setTextContent(String content) {
        rv_content.setAdapter(new VersionUpdateAdapter(context, content));
    }

    public void addTextContent(String content) {
        rv_content.setAdapter(new VersionUpdateAdapter(context, content));
    }

//    //setText(styledText, TextView.BufferType.SPANNABLE)
//    public void setTextContent(SpannableString styledText) {
//      //  tv_content.setText(styledText, TextView.BufferType.SPANNABLE);
//    }

    /**
     * 设置标题
     */
    public void setTextTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置取消按钮的字
     */
    public void setButtonContent(String content) {
        bt_cancel.setText(content);
    }

    /**
     * 设置OK按钮的字
     */
    public void setButtonOk(String content) {
        bt_ok.setText(content);
    }

    /**
     * 是否只有一个确定按钮
     */
    public void setOnlyOk(boolean b) {
        if (b) {
            bt_cancel.setVisibility(View.GONE);
            bt_ok.setBackgroundResource(R.drawable.dialog_ok2);
            ck.setVisibility(View.GONE);
        } else {
            bt_cancel.setVisibility(View.VISIBLE);
            bt_ok.setBackgroundResource(R.drawable.dialog_ok);
        }
    }
}