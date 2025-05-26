package com.ssafy.enjoytrip.everywhere.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.chat.dto.request.MyChatRequest;
import com.ssafy.enjoytrip.everywhere.chat.dto.response.MyChatResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_API_KEY;
    private static final String KEY_PREFIX = "chat_history:";
    private final RedisTemplate<String, String> userRedisTemplate;

    public MyChatResponse chat(MyChatRequest request) {
        String userKey = KEY_PREFIX + request.userId();

        // 1. Redis에서 히스토리 가져오기
        List<ChatMessage> history = getChatHistory(userKey);

        // 2. 현재 메시지를 추가
        ChatMessage userMessage = new ChatMessage("user", request.message());
        history.add(userMessage);

        // 3. OpenAI 호출
        OpenAiService service = new OpenAiService(OPENAI_API_KEY);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(history)
                .build();

        String reply = service.createChatCompletion(completionRequest)
                .getChoices().get(0).getMessage().getContent();

        // 4. 응답 메시지를 히스토리에 추가하고 저장
        ChatMessage assistantMessage = new ChatMessage("assistant", reply);
        saveChatMessage(userKey, userMessage);
        saveChatMessage(userKey, assistantMessage);

        return new MyChatResponse(reply);
    }

    private List<ChatMessage> getChatHistory(String userKey) {
        List<String> rawMessages = userRedisTemplate.opsForList().range(userKey, 0, -1);
        if (rawMessages == null) return new ArrayList<>();

        return rawMessages.stream()
                .map(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(json, ChatMessage.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void saveChatMessage(String userKey, ChatMessage message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);
            userRedisTemplate.opsForList().rightPush(userKey, json);

            // TTL이 설정되지 않은 경우만 expire 설정
            Long ttl = userRedisTemplate.getExpire(userKey);
            if (ttl == null || ttl == -1) {
                userRedisTemplate.expire(userKey, Duration.ofDays(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

