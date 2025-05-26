package com.ssafy.enjoytrip.everywhere.history.repository;

import java.util.List;

public interface HistoryRepository {
    void saveSearchKeyword(String userId, String keyword);
    List<String> getRecentKeywords(String userId);
}

