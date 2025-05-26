package com.ssafy.enjoytrip.everywhere.map.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionCacheInitializer {

    private final AttractionRepository attractionRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void cacheAttractions() {
        List<Attraction> attractions = attractionRepository.findAll();

        for (Attraction attraction : attractions) {
            try {
                String json = objectMapper.writeValueAsString(attraction);
                redisTemplate.opsForList().rightPush("attractions:all", json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        System.out.println("✅ Redis attraction 캐싱 완료");
    }
}
