package com.ssafy.enjoytrip.everywhere.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}