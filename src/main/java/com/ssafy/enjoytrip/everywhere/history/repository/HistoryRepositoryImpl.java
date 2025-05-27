package com.ssafy.enjoytrip.everywhere.history.repository;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepository {

    @Qualifier("attractionRedisRepository")
    private final AttractionRedisRepository attractionRepository;
    private final RedisTemplate<String, String> userRedisTemplate;

    private String getKey(String userId) {
        return "history:" + userId;
    }

    @Override
    public void saveSearchKeyword(String userId, String keyword) {
        String key = getKey(userId);
        userRedisTemplate.opsForList().leftPush(key, keyword);
        userRedisTemplate.opsForList().trim(key, 0, 4); // 최신 5개 유지
    }

    @Override
    public List<String> getRecentKeywords(String userId) {
        return userRedisTemplate.opsForList().range(getKey(userId), 0, 4);
    }

    @Override
    public List<AttractionSimpleResponse> historyFitAttraction(String userId) {
        List<AttractionSimpleResponse> results = new ArrayList<>();

        List<String> keywords = getRecentKeywords(userId);
        System.out.println(keywords);

        for(String keyword : keywords){
            List<AttractionSimpleResponse> re = attractionRepository.findByTitle(keyword);
            results.addAll(re);
        }
        return results.subList(0,10);
    }
}
