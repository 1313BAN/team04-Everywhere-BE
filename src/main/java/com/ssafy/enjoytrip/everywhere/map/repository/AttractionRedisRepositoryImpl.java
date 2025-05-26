package com.ssafy.enjoytrip.everywhere.map.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static javax.swing.UIManager.getString;

@Repository
@RequiredArgsConstructor
public class AttractionRedisRepositoryImpl implements  AttractionRedisRepository {


    private final RedisTemplate<String, String> redisTemplate;

    private AttractionRedis getAttraction(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        System.out.println(map.toString());
        if (map.isEmpty()) return null;

        try {
            AttractionRedis dto = new AttractionRedis();

            dto.setContentId(parseLong((String) map.get("content_id")));
            dto.setContentType(getString(map.get("content_type")));
            dto.setTitle(getString(map.get("title")));
            dto.setSiGunGuName(getString(map.get("si_gun_gu_name")));
            dto.setCategory(getString(map.get("category")));
            dto.setAreaCode(parseInt((String) map.get("area_code")));
            dto.setSiGunGuCode(parseInt((String) map.get("si_gun_gu_code")));
            String embeddingRaw = (String) map.get("embedding");
            if (embeddingRaw != null) {
                byte[] bytes = embeddingRaw.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
                dto.setEmbedding(byteArrayToFloatArray(bytes));
            }
//            System.out.println(dto.toString());
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private float[] byteArrayToFloatArray(byte[] bytes) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.LITTLE_ENDIAN);
        float[] floats = new float[bytes.length / 4];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = buffer.getFloat();
        }
        return floats;
    }

    private static final String KEY_PREFIX = "attraction:";

    @Override
    public List<AttractionRedis> findAll() {
        Set<String> keys = Objects.requireNonNull(redisTemplate.keys(KEY_PREFIX + "*"));
        return keys.stream()
                .map(this::getAttraction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<AttractionRedis> nearBy(long lat, long lon) {
        redisTemplate.opsForGeo().radius("attractions", new Circle(
                new Point(lat, lon),
                new Distance(5, Metrics.KILOMETERS)
        ));
        return new ArrayList<>();
    }

    public List<AttractionRedis> findById(long contentId) {
        return findAll().stream()
                .filter(attraction -> Objects.equals(attraction.getContentId(), contentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionRedis> findByContentType(String contentType) {
        return findAll().stream()
                .filter(attraction -> Objects.equals(attraction.getContentType(), contentType))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionRedis> findByCategory(String categoryCode) {
        System.out.println("impl");
        return findAll().stream()
                .filter(attraction -> categoryCode.equalsIgnoreCase(attraction.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionRedis> findByAreaCode(Integer areaCode) {
        return findAll().stream()
                .filter(attraction -> Objects.equals(attraction.getAreaCode(), areaCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionRedis> findByRegion(Integer areaCode, Integer siGunGuCode) {
        return findAll().stream()
                .filter(attraction -> Objects.equals(attraction.getAreaCode(), areaCode) &&
                        Objects.equals(attraction.getSiGunGuCode(), siGunGuCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionRedis> searchByKeywordEmbedding(String keyword) {
        // TODO: 1. 키워드를 벡터로 임베딩
        // TODO: 2. Redis 벡터 필드에 대해 KNN 검색 (벡터 유사도)
        // ⚠️ 일반 Java에서는 구현 어려움 -> Python 서버와 연동 추천
        return new ArrayList<>();
    }

}
