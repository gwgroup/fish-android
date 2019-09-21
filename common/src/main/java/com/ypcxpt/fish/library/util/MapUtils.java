package com.ypcxpt.fish.library.util;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapUtils {

    /**
     * @param map
     * @return
     */
    public static <K, V> List<K> getKeys(Map<K, V> map) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }

        ArrayList<K> list = new ArrayList<>();
        Iterator iterator = getIterator(map);

        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry) iterator.next();
            list.add(entry.getKey());
        }

        return list;
    }

    /**
     * @param map
     * @return
     */
    public static <K, V> List<V> getValues(Map<K, V> map) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }

        ArrayList<V> list = new ArrayList<>();
        Iterator iterator = getIterator(map);

        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry) iterator.next();
            list.add(entry.getValue());
        }

        return list;
    }

    private static Iterator getIterator(Map map) {
        return map == null ? null : map.entrySet().iterator();
    }

    /**
     * is null or its size is 0
     * <p/>
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     *
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
