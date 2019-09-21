package com.ypcxpt.fish.library.router;

import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.ypcxpt.fish.library.R;

public class ActivityTransitionAnim {
    /** Enter from left , exit to right. */
    private static int[] LEFT_IN_ANIM = {R.anim.slide_in_from_left, R.anim.slide_out_to_right};

    /** Enter from right , exit to left. */
    private static int[] RIGHT_IN_ANIM = {R.anim.slide_in_from_right, R.anim.slide_out_to_left};

    private static int[] HOME_FINISH = {R.anim.home_enter, R.anim.home_exit};

    /**
     * @param source
     * @param startStyle
     */
    public static ActivityOptionsCompat getCompatWithView(View source, StartStyle startStyle) {
        return getCompatWithoutView(source.getContext(), startStyle);
    }

    /**
     * @param context
     * @param startStyle
     */
    public static ActivityOptionsCompat getCompatWithoutView(
            Context context, StartStyle startStyle) {
        return ActivityOptionsCompat.makeCustomAnimation(
                context, getStartEnterAnim(startStyle), getStartExitAnim(startStyle));
    }

    /** Default enter_anim is from right to left. */
    public static int getStartEnterAnim(StartStyle startStyle) {
        if (startStyle == null) {
            return getStartEnterAnim(StartStyle.COMMON);
        }

        switch (startStyle) {
            default:
            case COMMON:
                return RIGHT_IN_ANIM[0];
            case REVERSE:
                return LEFT_IN_ANIM[0];
        }
    }

    /** Default exit_anim is from left to right. */
    public static int getStartExitAnim(StartStyle startStyle) {
        if (startStyle == null) {
            return getStartExitAnim(StartStyle.COMMON);
        }

        switch (startStyle) {
            default:
            case COMMON:
                return RIGHT_IN_ANIM[1];
            case REVERSE:
                return LEFT_IN_ANIM[1];
        }
    }

    /** Default enter_anim is from left to right. */
    public static int getFinishEnterAnim(FinishStyle finishStyle) {
        if (finishStyle == null) {
            return getFinishEnterAnim(FinishStyle.COMMON);
        }

        switch (finishStyle) {
            default:
            case COMMON:
                return LEFT_IN_ANIM[0];
            case HOME:
                return HOME_FINISH[0];
        }
    }

    /** Default exit_anim is from right to left. */
    public static int getFinishExitAnim(FinishStyle finishStyle) {
        if (finishStyle == null) {
            return getFinishExitAnim(FinishStyle.COMMON);
        }

        switch (finishStyle) {
            default:
            case COMMON:
                return LEFT_IN_ANIM[1];
            case HOME:
                return HOME_FINISH[1];
        }
    }
}
