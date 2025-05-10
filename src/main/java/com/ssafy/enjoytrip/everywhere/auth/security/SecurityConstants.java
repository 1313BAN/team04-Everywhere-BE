package com.ssafy.enjoytrip.everywhere.auth.security;

public final class SecurityConstants {

    private SecurityConstants() {}

    public static final String HEADER_AUTH = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7; // 7일

}