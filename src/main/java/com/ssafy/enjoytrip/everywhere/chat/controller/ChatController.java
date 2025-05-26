package com.ssafy.enjoytrip.everywhere.chat.controller;

import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.chat.dto.request.MyChatRequest;
import com.ssafy.enjoytrip.everywhere.chat.dto.response.MyChatResponse;
import com.ssafy.enjoytrip.everywhere.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public MyChatResponse chat(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody String message) {

        MyChatRequest request = new MyChatRequest(userDetails.getUsername(), message);
        return chatService.chat(request);
    }
}
