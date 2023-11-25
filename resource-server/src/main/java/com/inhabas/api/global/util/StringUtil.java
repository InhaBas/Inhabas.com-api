package com.inhabas.api.global.util;

public class StringUtil {

    private static final String ONLY_DIGIT_PATTERN = "\\d+";

    public static boolean isNumeric(String str) {
        return str.matches(ONLY_DIGIT_PATTERN);
    }
}
