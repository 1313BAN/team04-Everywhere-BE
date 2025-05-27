package com.ssafy.enjoytrip.everywhere.chat.dto.request;

import lombok.Builder;

@Builder
public record MyChatRequest(String message) {
    public String getMessage() { return message;}
}
