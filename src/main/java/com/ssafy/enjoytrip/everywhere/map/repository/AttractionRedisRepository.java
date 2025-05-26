package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationRequest;
import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import io.redisearch.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRedisRepository {

    List<AttractionSimpleResponse> findAll();
    AttractionSimpleResponse findByContentId(Long contentId);
    List<AttractionSimpleResponse> findByContentType(String contentType);
    List<AttractionSimpleResponse> findByCategory(String categoryCode);
//    Document searchByKeywordEmbedding(String keyword); // 내부에서 벡터화 및 KNN 수행
//    Document findByRegion(Integer areaCode, Integer siGunGuCode);
    List<AttractionSimpleResponse>  findByAreaCode(String areaName);
    List<AttractionSimpleResponse> getNearBy(LocationSearchRequest locationRequest);
//    AttractionsResponse findAttractionsByIds(List<Long> contentIds);
}
