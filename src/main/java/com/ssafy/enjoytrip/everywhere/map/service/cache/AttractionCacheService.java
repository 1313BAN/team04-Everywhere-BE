package com.ssafy.enjoytrip.everywhere.map.service.cache;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionRedisResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionCacheService {

    private final AttractionRepository attractionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheAttractions() {
        List<Attraction> attractions = attractionRepository.findAll();

        for (Attraction attraction : attractions) {
            AttractionRedisResponse dto = new AttractionRedisResponse(
                    attraction.getContentId(),
                    attraction.getTitle(),
                    attraction.getLatitude(),
                    attraction.getLongitude(),
                    attraction.getAddress(),
                    attraction.getFirstImage(),
                    attraction.getCategory()
            );

            redisTemplate.opsForHash().put("attractions:hash", String.valueOf(dto.getContentId()), dto);
        }

        System.out.println("✅ Redis 캐시 적재 완료");
    }
}
