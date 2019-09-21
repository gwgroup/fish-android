package com.ypcxpt.fish.core.model;

import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;

public class DeviceAction extends PopupItem {
    /* 操作指令码，只接受16进制整型 */
    public int code;

    /* 是否是长按按钮 */
    public boolean longClick;

    public DeviceAction(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public DeviceAction(int code, String name, boolean longClick) {
        this.code = code;
        this.name = name;
        this.longClick = longClick;
    }

    @Override
    public String toString() {
        return "DeviceAction{" +
                "code=" + code +
                ", longClick=" + longClick +
                ", name='" + name + '\'' +
                '}';
    }
}
