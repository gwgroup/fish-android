package com.ypcxpt.fish.library.util;

public class FormatUtils {

    public static final boolean DEFAULT_BOOLEAN = false;

    public static final int DEFAULT_INT = 0;

    public static final double DEFAULT_DOUBLE = 0D;

    public static boolean getBoolean(String str) {
        return getBoolean(str, DEFAULT_BOOLEAN);
    }

    public static boolean getBoolean(String str, boolean defaults) {
        try {
            return Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            return defaults;
        }
    }

    public static int getInt(String str) {
        return getInt(str, DEFAULT_INT);
    }

    public static int getInt(String str, int defaults) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaults;
        }
    }

    public static double getDouble(String str) {
        return getDouble(str, DEFAULT_DOUBLE);
    }

    public static double getDouble(String str, double defaults) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaults;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 不满2位的数字，补0显示.
     */
    public static String keep2Places(int i) {
        return String.format("%02d", i);
    }

}
