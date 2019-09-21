package com.ypcxpt.fish.library.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.util.AnimationUtils;

public class PageLoadingView extends Dialog {
    private ImageView ivAnim;

    private Animation mAnim;

    public PageLoadingView(@NonNull Context context) {
        super(context, R.style.dialog_dim);
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_view, null);
        setContentView(view);

        ivAnim = view.findViewById(R.id.iv_anim);
        mAnim = AnimationUtils.infiniteRotateAnim(300);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        startAnim();
        super.show();
    }

    @Override
    public void dismiss() {
        stopAnim();
        super.dismiss();
    }

    private void startAnim() {
        ivAnim.startAnimation(mAnim);
    }

    private void stopAnim() {
        ivAnim.clearAnimation();
    }

}
