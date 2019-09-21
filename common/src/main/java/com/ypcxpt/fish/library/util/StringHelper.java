package com.ypcxpt.fish.library.util;

import com.blankj.utilcode.util.StringUtils;

public class StringHelper {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String nullToEmpty(String target) {
        return StringUtils.isTrimEmpty(target) ? "" : target;
    }

    public static String nullToDefault(String target, String defaultStr) {
        return StringUtils.isTrimEmpty(target) ? defaultStr : target;
    }

}
