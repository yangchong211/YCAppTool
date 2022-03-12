package com.yc.alive.util;

import androidx.annotation.RestrictTo;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * String 工具
 * <p>
 *  2018/5/16.
 */
@RestrictTo(LIBRARY)
public class AliveStringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
