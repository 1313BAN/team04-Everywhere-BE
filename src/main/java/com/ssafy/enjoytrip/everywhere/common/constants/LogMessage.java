package com.ssafy.enjoytrip.everywhere.common.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LogMessage {
    API_REQUEST("[API 요청] [{}] {}{} from {}"),
    API_RESPONSE("[API 응답 완료] Status: {}"),
    EXCEPTION_OCCURRED("[예외 발생] {}");

    private final String template;

    LogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        Object[] safeArgs = Arrays.stream(args)
                .map(arg -> arg == null ? "" : arg)
                .toArray();
        return String.format(this.template.replace("{}", "%s"), safeArgs);
    }

}
