package com.ssafy.enjoytrip.everywhere.common.dto.response;

import com.ssafy.enjoytrip.everywhere.common.constants.Status;
import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;

public record ApiResponse<T>(
        Status status,
        Enum<?> code,
        T data
) {
    public static <T> ApiResponse<T> success(Enum<?> code, T data) {
        return new ApiResponse<>(Status.SUCCESS, code, data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(Status.SUCCESS, SuccessCode.SUCCESS_DEFAULT, null);
    }

    public static <T> ApiResponse<T> error(Enum<?> code) {
        return new ApiResponse<>(Status.ERROR, code, null);
    }

    public static <T> ApiResponse<T> success(Enum<?> code) {
        return new ApiResponse<>(Status.SUCCESS, code, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Status.SUCCESS, SuccessCode.SUCCESS_DEFAULT, data);
    }
}