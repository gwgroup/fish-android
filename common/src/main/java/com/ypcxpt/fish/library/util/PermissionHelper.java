package com.ypcxpt.fish.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import com.ypcxpt.fish.library.listener.RequestPermissionListener;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private List<String> deniedPermissions;

    /**
     * 验证某个权限是否已授权
     */
    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请权限通用方法.
     *
     * @param activity    Activity级为目标
     * @param listener    权限申请的结果回调
     * @param permissions 待申请权限列表
     */
    public void requestPermissions(Activity activity, RequestPermissionListener listener, String[] permissions) {
        if (listener == null) {
            return;
        }

        if (ObjectUtils.isEmpty(permissions)) {
            listener.onAcceptAllPermissions();
            return;
        }

        deniedPermissions = new ArrayList<>();

        new RxPermissions(activity)
                .requestEach(permissions)
                .doOnComplete(() -> {
                    if (deniedPermissions.size() == 0) {
                        deniedPermissions = null;
                        listener.onAcceptAllPermissions();
                    } else {
                        listener.onDenySomePermissions(deniedPermissions);
                    }
                })
                .subscribe(permission -> {
                    if (!permission.granted) {
                        deniedPermissions.add(permission.name);
                    }
                });
    }

}
