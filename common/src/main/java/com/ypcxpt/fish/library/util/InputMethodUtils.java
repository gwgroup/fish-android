package com.ypcxpt.fish.library.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtils {
    /**
     * 拿到输入法Manager.
     */
    public static InputMethodManager getIMM(Activity activity) {
        return ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
    }

    /**
     * 自动开启输入法.
     */
    public static void autoShowInputMethod(Activity activity) {
        ThreadHelper.postDelayed(() -> getIMM(activity).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0),
                300);
    }
}
