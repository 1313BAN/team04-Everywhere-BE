package com.ssafy.enjoytrip.everywhere.auth.jwt;

public enum JwtConstants {
    AUTH_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer "),
    BLACKLIST("bl_"),

    CLAIM_TYPE("type"),
    CLAIM_NAME("name"),
    CLAIM_ROLE("role"),
    LOGOUT("logout"),

    TOKEN_TYPE_ACCESS("access"),
    TOKEN_TYPE_REFRESH("refresh");

    private final String value;

    JwtConstants(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}