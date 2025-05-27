package com.ssafy.enjoytrip.everywhere.history.repository;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;

import java.util.List;

public interface HistoryRepository {
    void saveSearchKeyword(String userId, String keyword);
    List<String> getRecentKeywords(String userId);
    List<AttractionSimpleResponse> historyFitAttraction(String userId);
}

