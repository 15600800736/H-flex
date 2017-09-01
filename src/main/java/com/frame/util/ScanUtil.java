package com.frame.util;

import java.util.Map;

/**
 * Created by fdh on 2017/8/31.
 */
public class ScanUtil {
    public static boolean isTypeAlias(String text) {
        return text.startsWith("[") && text.endsWith("]");
    }

    public static String getRealType(String text, Map<String, String> typeAlias) {
        if (!isTypeAlias(text) || typeAlias == null) {
            return text;
        } else {
            return typeAlias.get(text.substring(1, text.length() - 1));
        }
    }
}
