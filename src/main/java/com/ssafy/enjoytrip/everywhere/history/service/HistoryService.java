package com.ssafy.enjoytrip.everywhere.history.service;

import com.ssafy.enjoytrip.everywhere.history.repository.HistoryRepository;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public void addKeyword(String userId, String keyword) {
        historyRepository.saveSearchKeyword(userId, keyword);
    }

    public List<String> getRecentKeywords(String userId) {
        return historyRepository.getRecentKeywords(userId);
    }

    public List<AttractionSimpleResponse> historyFitAttraction(String userId) {
        return historyRepository.historyFitAttraction(userId);
    }

}

