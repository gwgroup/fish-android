package com.ypcxpt.fish.core.ble.input;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.library.util.FormatUtils;
import com.ypcxpt.fish.library.util.HexString;
import com.ypcxpt.fish.library.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 按摩椅向手机发送的数据帧:
 * 包头	  包头    数据长度  反馈数据	数据类型	 状态字0  ...   状态字8	   校验码
 * 0x23	  0x75	  0x10	  0x__		0x__	 0x__    ...   0x__        ____
 * <p>
 */
public class T100DataParser {

    /**
     * @param text 接收到的原始数据.
     * @return 数据0 ~ 数据10.(这里去掉了两个包头和最后一位校验码)
     */
    public List<Integer> parse(String text) {
        if (StringUtils.isTrimEmpty(text)) return null;

        int T100_SECTIONS = text.length()/2;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < T100_SECTIONS; i++) {
            if (i == 0 || i == 1 || i == T100_SECTIONS - 1) {
                continue;
            }
            String section = text.substring(i * 2, i * 2 + 2);
            list.add(HexString.hexStringToInt(section));
        }

        ListUtils.printHexString(list);
        return list;
    }

    /**
     * 数据9：	按摩时间(分钟)
     * 数据10：	按摩时间(秒)
     *
     * @return "00:00"
     */
    public String getTime(List<Integer> parsedData) {
        int minutes = parsedData.get(9);
        int seconds = parsedData.get(10);
        return FormatUtils.keep2Places(minutes) + ":" + FormatUtils.keep2Places(seconds);
    }

    public static T100DataParser get() {
        return Singleton.sInstance;
    }

    private T100DataParser() {
    }

    private static class Singleton {
        private static T100DataParser sInstance = new T100DataParser();
    }
}
