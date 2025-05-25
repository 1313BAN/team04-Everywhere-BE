package com.ssafy.enjoytrip.everywhere.history.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private String getKey(String userId) {
        return "history:" + userId;
    }

    @Override
    public void saveSearchKeyword(String userId, String keyword) {
        String key = getKey(userId);
        redisTemplate.opsForList().leftPush(key, keyword);
        redisTemplate.opsForList().trim(key, 0, 4); // 최신 5개 유지
    }

    @Override
    public List<String> getRecentKeywords(String userId) {
        return redisTemplate.opsForList().range(getKey(userId), 0, 4);
    }
}
