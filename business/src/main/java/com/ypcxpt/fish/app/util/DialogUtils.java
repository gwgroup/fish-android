package com.ypcxpt.fish.app.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypcxpt.fish.R;

/**
 * @name Login
 * @class name：com.vincent.cloud.util
 * @class describe
 * @anthor Vincent
 * @time 2017/7/21 10:47
 * @change
 * @chang time
 * @class describe
 */

public class DialogUtils {

    /**
     * 分享
     * @param activity activity
     * @param content 分享的内容
     * @param listener 监听
     */
    public static void showSharedDialog(Activity activity, final String content, final SharedListener listener){
        final Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_shared, null,false);
        LinearLayout llWX = contentView.findViewById(R.id.dlg_shared_wx_friend);
        LinearLayout llWXC = contentView.findViewById(R.id.dlg_shared_wx_friend_circle);
        LinearLayout llWXCollect = contentView.findViewById(R.id.dlg_shared_wx_collect);
        TextView cancel = contentView.findViewById(R.id.dlg_shared_cancel);
        cancel.setOnClickListener(view -> bottomDialog.dismiss());
        llWX.setOnClickListener(view -> {
            listener.sharedToWXFriend(content);
            bottomDialog.dismiss();
        });
        llWXC.setOnClickListener(view -> {
            listener.sharedToWXFriendCircle(content);
            bottomDialog.dismiss();
        });
        llWXCollect.setOnClickListener(view -> {
            listener.sharedToWXCollect(content);
            bottomDialog.dismiss();
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }

    public interface SharedListener{

        void sharedToWXFriend(String content);

        void sharedToWXFriendCircle(String content);

        void sharedToWXCollect(String content);
    }

}
