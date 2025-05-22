package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRedisRepository {

    List<AttractionRedis> findAll();
    List<AttractionRedis> findByContentTypeId(Integer contentTypeId);
    List<AttractionRedis> findByCategory(String categoryCode);
    List<AttractionRedis> searchByKeywordEmbedding(String keyword); // 내부에서 벡터화 및 KNN 수행
    List<AttractionRedis> findByRegion(Integer areaCode, Integer siGunGuCode);

}
