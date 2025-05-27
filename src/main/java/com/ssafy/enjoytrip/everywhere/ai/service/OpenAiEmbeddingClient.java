package com.ssafy.enjoytrip.everywhere.ai.service;

import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OpenAiEmbeddingClient {

    private final OpenAiService openAiService;

    public OpenAiEmbeddingClient(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public List<Float> getEmbedding(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Embedding input must be a non-empty string");
        }

        EmbeddingRequest request = EmbeddingRequest.builder()
                .model("text-embedding-3-large")
                .input(List.of(text))
                .build();

        EmbeddingResult result = openAiService.createEmbeddings(request);
        return result.getData().get(0).getEmbedding().stream()
                .map(Double::floatValue)
                .toList();
    }
}
