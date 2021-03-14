package com.zahjava.ecommercebackend.utils;

public final class UrlUtil {
    private UrlUtil() {
    }

    private static String allPrefix = "/*";
    public static String permitAllUrl[] = {
            UrlConstraint.AuthManagement.ROOT + allPrefix
    };
}
