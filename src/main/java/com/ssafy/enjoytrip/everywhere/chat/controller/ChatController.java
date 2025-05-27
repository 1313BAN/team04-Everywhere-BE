package com.ssafy.enjoytrip.everywhere.chat.controller;


import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.chat.dto.request.MyChatRequest;
import com.ssafy.enjoytrip.everywhere.chat.dto.response.MyChatResponse;
import com.ssafy.enjoytrip.everywhere.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<MyChatResponse> chat(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MyChatRequest request
    ) {
        String userId = userDetails.getUsername();
        log.info("Received chat request from user: {}", userId);

        try {
            String response = chatService.generateResponse(userId, request.getMessage());
            return ResponseEntity.ok(new MyChatResponse(response));
        } catch (Exception e) {
            log.error("Error processing chat request for user: {}", userId, e);
            return ResponseEntity.internalServerError()
                    .body(new MyChatResponse("죄송합니다. 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/custom")
    public ResponseEntity<MyChatResponse> chatWithOptions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MyChatRequest request,
            @RequestParam(defaultValue = "0.7") double temperature,
            @RequestParam(defaultValue = "qwen2.5:7b") String model) {
        String userId = userDetails.getUsername();

        log.info("Received custom chat request from user: {} with model: {}", userId, model);

        try {
            String response = chatService.generateWithOptions(
                    userId,
                    request.getMessage(),
                    temperature,
                    model
            );
            return ResponseEntity.ok(new MyChatResponse(response));
        } catch (Exception e) {
            log.error("Error processing custom chat request for user: {}", userId, e);
            return ResponseEntity.internalServerError()
                    .body(new MyChatResponse("죄송합니다. 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Ollama Chat API가 정상적으로 작동중입니다!");
    }
}