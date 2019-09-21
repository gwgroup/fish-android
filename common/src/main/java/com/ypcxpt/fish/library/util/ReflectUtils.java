package com.ypcxpt.fish.library.util;

import com.blankj.utilcode.util.StringUtils;

import java.lang.reflect.Field;

public class ReflectUtils {
    /**
     * @param target
     * @param filedName
     * @param value
     * @param <T>
     */
    public static <T> void setFiledValue(T target, String filedName, Object value) {
        try {
            Field declaredField = getField(target.getClass(), filedName);
            if (declaredField == null) return;

            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);

            declaredField.set(target, value);

            declaredField.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> Field getField(Class<?> clazz, String filedName) {
        if (clazz == null || StringUtils.isTrimEmpty(filedName)) return null;

        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(filedName);
        } catch (Exception e) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), filedName);
            }
        }
        return declaredField;
    }

}
