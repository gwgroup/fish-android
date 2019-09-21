package com.ypcxpt.fish.library.util;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.List;

public class ListUtils {

    public static void printHexString(List<Integer> list) {
        if (ObjectUtils.isEmpty(list)) return;
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sBuilder.append(i + "段=" + HexString.toHexString(list.get(i)) + " ");
        }
        Logger.d("CCC", sBuilder.toString());
    }
}
