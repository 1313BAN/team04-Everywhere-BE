package com.ssafy.enjoytrip.everywhere.auth.dto.response;

public record LoginResponse(String accessToken, String refreshToken, String nickname) {
}