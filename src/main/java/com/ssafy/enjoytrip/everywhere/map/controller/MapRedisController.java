package com.ssafy.enjoytrip.everywhere.map.controller;

import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationRequest;
import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.request.AttractionRequest;
import com.ssafy.enjoytrip.everywhere.map.service.MapRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map/redis")
@RequiredArgsConstructor
public class MapRedisController {

    private final MapRedisService mapService;

    @GetMapping
    public  ResponseEntity<AttractionsResponse> attractions(){
        List<AttractionSimpleResponse> results = mapService.getAll();
        return ResponseEntity.ok(new AttractionsResponse(results));
    }

    @PostMapping("/areaName")
    public ResponseEntity<AttractionsResponse> keywordFilter(@RequestBody AttractionRequest request) {
        // ex. 서울, 인천 등등
        String areaName = request.getQuery();
        List<AttractionSimpleResponse> results = mapService.getByAreaCode(areaName);
        return ResponseEntity.ok(new AttractionsResponse(results));
    }

    @PostMapping("/category")
    public ResponseEntity<AttractionsResponse> categoryFilter(@RequestBody AttractionRequest request) {
        String category = request.getQuery(); // 카테고리명도 keyword 필드로 받는다고 가정
        List<AttractionSimpleResponse> results = mapService.getByCategory(category);
        return ResponseEntity.ok(new AttractionsResponse(results));
    }

    @PostMapping("/contentType")
    public ResponseEntity<AttractionsResponse> contentTypeFilter(@RequestBody AttractionRequest request) {
        String typeName = request.getQuery();
        List<AttractionSimpleResponse> results = mapService.getByContentType(typeName);
        return ResponseEntity.ok(new AttractionsResponse(results));
    }

    @PostMapping("/contentId")
    public ResponseEntity<AttractionSimpleResponse> getById(@RequestBody AttractionRequest request) {
        long id = Long.parseLong(request.getQuery());  // contentId도 keyword 필드로 받음
        AttractionSimpleResponse result = mapService.getByContentId(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/location")
    public ResponseEntity<List<AttractionSimpleResponse>> getNearBy(@RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        LocationSearchRequest request = new LocationSearchRequest(longitude, latitude,5);

        List<AttractionSimpleResponse> result = mapService.getNearBy(request);
        return ResponseEntity.ok(result);
    }


}
