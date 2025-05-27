package com.ssafy.enjoytrip.everywhere.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;

    public String generateResponse(String userId, String userMessage) {
        log.info("Generating response for user: {}, message: {}", userId, userMessage);

        // 한국어 맥락을 위한 시스템 프롬프트
        String systemPrompt = """
당신은 여행 전문가이다. 사용자의 질문에 친근한 톤의 한국어로 답변하여라.""";

        PromptTemplate promptTemplate = new PromptTemplate("""
{system_prompt}
사용자 질문: {user_message}
답변:""");

        Prompt prompt = promptTemplate.create(Map.of(
                "system_prompt", systemPrompt,
                "user_message", userMessage
        ));

        String response = chatModel.call(prompt).getResult().getOutput().getText();

        return response;
    }

    public String routeResponse(String userId, String routes, String restaurants) {
        log.info("Generating response for user: {}, message: {}", userId, routes);

        // 한국어 맥락을 위한 시스템 프롬프트
        String systemPrompt = """
당신은 여행 경로 최적화 전문가이다.
사용자는 여행하려는 장소들과 그 주변 식당 정보를 제공함
각 장소와 식당은 이름(title)과 위도(latitude), 경도(longitude), content_id로 구성되며,
당신의 임무는 사용자가 입력한 장소들의 위치 정보를 기반으로 최적의 여행 경로를 계산하는 것이다.
여행 경로 답게 이동하는 중간에 "restaurants" 방문을 할 수 있어야 한다.
최적화 기준은 이동 거리 최소화이다.

반드시 여행지들의 content_id만으로 구성된 리스트를 반환해야 함에 주의.
{출력 형식 예시}
input:
"attractions": [
    {'name': "경복궁", 'latitude': 37.5796, 'longitude': 126.9770, 'content_id': 1001},
    {'name': "남산타워", 'latitude': 37.5512, 'longitude': 126.9882, 'content_id': 1002},
    {'name': "롯데월드", 'latitude': 37.5110, 'longitude': 127.0980, 'content_id': 1003}
],
"restaurants": [
    {'name': "한옥집", 'latitude': 37.5789, 'longitude': 126.9765, 'content_id': 2001},
    {'name': "타워레스토랑", 'latitude': 37.5511, 'longitude': 126.9880, 'content_id': 2002}
]
return: [1001, 1002, 1003]""";

        PromptTemplate promptTemplate = new PromptTemplate("""
{system_prompt}

input:
"attractions": [
    {places}
]
"restaurants": [
    {foods}
]
return:""");

        Prompt prompt = promptTemplate.create(Map.of(
                "system_prompt", systemPrompt,
                "places", routes,
                "foods", restaurants
        ));

        System.out.println("👍👍👍👍👍👍👍");
        System.out.println(prompt.toString());

        String response = chatModel.call(prompt).getResult().getOutput().getText();
        log.info("Generated response for user: {}", userId);

        return response;
    }

    public String generateWithOptions(String userId, String userMessage, Double temperature, String model) {
        log.info("Generating custom response for user: {}, model: {}, temperature: {}", userId, model, temperature);

        OllamaOptions options = new OllamaOptions();
        options.setModel(model);
        options.setTemperature(temperature);

        Prompt prompt = new Prompt(userMessage, options);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}