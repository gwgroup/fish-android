package com.ypcxpt.fish.library.listener;

import java.util.List;

public interface RequestPermissionListener {
    /**
     * 小于{@link android.os.Build.VERSION_CODES#M}必然调用，
     * 大于或者等于时，只有申请的权限全部同意才会调用
     */
    void onAcceptAllPermissions();

    /**
     * 该方法只有在大于或者等于{@link android.os.Build.VERSION_CODES#M}才有可能被调用
     */
    void onDenySomePermissions(List<String> deniedPermissions);
}
