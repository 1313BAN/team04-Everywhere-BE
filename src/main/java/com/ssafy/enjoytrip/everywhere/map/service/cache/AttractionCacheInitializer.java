package com.ssafy.enjoytrip.everywhere.map.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionRedisResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionCacheInitializer {

    private final AttractionRepository attractionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
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

        System.out.println("✅ Redis attraction 캐싱 완료 (Hash + 필드 축소)");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}