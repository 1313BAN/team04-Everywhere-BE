package com.ssafy.enjoytrip.everywhere.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname
) {}

