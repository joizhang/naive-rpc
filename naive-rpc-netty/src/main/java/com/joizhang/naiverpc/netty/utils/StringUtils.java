package com.joizhang.naiverpc.netty.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class StringUtils {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * split.
     *
     * @param ch char.
     * @return string array.
     */
    public static String[] split(String str, char ch) {
        if (isEmpty(str)) {
            return EMPTY_STRING_ARRAY;
        }
        return splitToList0(str, ch).toArray(EMPTY_STRING_ARRAY);
    }

    private static List<String> splitToList0(String str, char ch) {
        List<String> result = new ArrayList<>();
        int ix = 0, len = str.length();
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == ch) {
                result.add(str.substring(ix, i));
                ix = i + 1;
            }
        }

        if (ix >= 0) {
            result.add(str.substring(ix));
        }
        return result;
    }

}

