package com.ssafy.enjoytrip.everywhere.hotplace.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HotplaceRepositoryImpl implements HotplaceRepository {

    private final String KEY = "hotplace:";
    private final String RANK_KEY = "hotplace:ranking";
    private final RedisTemplate<String, String> redisTemplate;

    private String getKey(String userId) {
        return KEY + userId;
    }

    @Override
    public void addHotplace(String userId, Long attractionId) {
        redisTemplate.opsForSet().add(getKey(userId), attractionId.toString());
    }

    @Override
    public void removeHotplace(String userId, Long attractionId) {
        redisTemplate.opsForSet().remove(getKey(userId), attractionId.toString());
    }

    @Override
    public List<Long> getHotplaceIdsByUser(String userId) {
        Set<String> ids = redisTemplate.opsForSet().members(getKey(userId));
        if (ids == null) return Collections.emptyList();
        return ids.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public void increaseScore(Long attractionId) {
        redisTemplate.opsForZSet().incrementScore(RANK_KEY, attractionId.toString(), 1.0);
    }

    @Override
    public List<Long> getTopHotplaces(int limit) {
        Set<String> ids = redisTemplate.opsForZSet().reverseRange(RANK_KEY, 0, limit - 1);
        if (ids == null) return Collections.emptyList();
        return ids.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public boolean existsHotplace(String userId, Long attractionId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(KEY + userId, attractionId.toString())
        );
    }

}
