package com.ssafy.enjoytrip.everywhere.auth.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenResolver {

    public String resolve(String header) {
        if (header != null && header.startsWith(JwtConstants.TOKEN_PREFIX.value())) {
            return header.substring(JwtConstants.TOKEN_PREFIX.value().length());
        }
        return null;
    }
}