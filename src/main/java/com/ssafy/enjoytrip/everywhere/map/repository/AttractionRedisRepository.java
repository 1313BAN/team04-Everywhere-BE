package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.map.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.request.NearAttractionRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRedisRepository {

    List<AttractionSimpleResponse> findAll();
    AttractionSimpleResponse findByContentId(Long contentId);
    List<AttractionSimpleResponse> findByContentType(String contentType);
    List<AttractionSimpleResponse> findByCategory(String categoryCode);
    List<AttractionSimpleResponse>  findByAreaCode(String areaName);
    List<AttractionSimpleResponse> findNearBy(LocationSearchRequest locationRequest);
    List<AttractionSimpleResponse> findByTitle(String keyword);
    List<AttractionSimpleResponse> findByContentIds(List<Long> contentIds);

    List<AttractionSimpleResponse> searchByKeywordEmbedding(String keyword); // 내부에서 벡터화 및 KNN 수행
    List<AttractionSimpleResponse> findNearByLocationAndCategory(NearAttractionRequest request);

//    AttractionsResponse findAttractionsByIds(List<Long> contentIds);
//    Document findByRegion(Integer areaCode, Integer siGunGuCode);
}
