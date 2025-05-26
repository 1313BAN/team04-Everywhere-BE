package com.ssafy.enjoytrip.everywhere.map.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionRedisResponse;
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
            AttractionRedisResponse dto = new AttractionRedisResponse(
                    attraction.getContentId(),
                    attraction.getTitle(),
                    attraction.getContentTypeId(),
                    attraction.getLatitude(),
                    attraction.getLongitude(),
                    attraction.getAddress(),
                    attraction.getFirstImage(),
                    attraction.getCategory()
            );

            try {
                String json = objectMapper.writeValueAsString(dto);
                redisTemplate.opsForList().rightPush("attractions:all", json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        System.out.println("✅ Redis attraction 캐싱 완료 (최적화 DTO)");
    }
}
