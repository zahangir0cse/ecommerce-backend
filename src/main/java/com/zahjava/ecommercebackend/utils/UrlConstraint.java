package com.zahjava.ecommercebackend.utils;

public final class UrlConstraint {
    private UrlConstraint() {
    }


    private static final String VERSION = "/v1";
    private static final String API = "/api";

    public static class AuthManagement {
        public static final String ROOT = API + "/auth";
        public static final String LOGIN = "/login";
    }

    public static class CompanyManagement {
        public static final String ROOT = API + "/company";
        public static final String CREATE = "/create";
        public static final String UPDATE = "/{id}";
        public static final String GET = "/{id}";
        public static final String GET_ALL = "/all";
    }
}
