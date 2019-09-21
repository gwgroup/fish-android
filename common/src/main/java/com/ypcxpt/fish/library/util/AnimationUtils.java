package com.ypcxpt.fish.library.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class AnimationUtils {

    /**
     * @param duration 动画时长.
     * @return 无限循环的围绕自中心旋转的动画.
     */
    public static RotateAnimation infiniteRotateAnim(int duration) {
        //放大倍数
        int multiplier = 10000;
        int toDegrees = 359;
        toDegrees *= multiplier;
        duration *= multiplier;

        RotateAnimation anim = new RotateAnimation(0, toDegrees, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.RESTART);
        anim.setInterpolator(new LinearInterpolator());

        return anim;
    }

    public  static Animation getHorizontalAnimation(float start, float end, int durationMillis) {
        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, start, Animation.RELATIVE_TO_PARENT, end, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(durationMillis);
        return translateAnimation;
    }

}
