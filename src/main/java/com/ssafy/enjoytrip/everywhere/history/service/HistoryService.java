package com.ssafy.enjoytrip.everywhere.history.service;

import com.ssafy.enjoytrip.everywhere.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final WebClient webClient;  // DI로 주입
    private final HistoryRepository historyRepository;

    public void addKeyword(String userId, String keyword) {
        historyRepository.saveSearchKeyword(userId, keyword);
    }

    public List<String> getRecentKeywords(String userId) {
        return historyRepository.getRecentKeywords(userId);
    }

}

