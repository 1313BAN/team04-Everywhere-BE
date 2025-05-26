package com.ssafy.enjoytrip.everywhere.map.controller;

import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapRedisService;
import com.ssafy.enjoytrip.everywhere.map.service.MapService;
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
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getAllFromRedis() {
        List<AttractionSimpleResponse> result = mapService.getAllAttractionsFromRedis();
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, result));
    }

    @GetMapping("/{contentTypeId}")
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getByContentTypeFromRedis(
            @PathVariable String contentTypeId) {
        List<AttractionSimpleResponse> result = mapService.getByContentTypeFromRedis(contentTypeId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_BY_TYPE, result));
    }

    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getByCategoryFromRedis(
            @PathVariable String categoryCode) {
        System.out.println("categoryCode:"+categoryCode);
        List<AttractionSimpleResponse> result = mapService.getByCategoryFromRedis(categoryCode);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, result));
    }

    @PostMapping("/keyword")
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getByKeywordFromRedis(
            @RequestParam String keyword) {
        List<AttractionSimpleResponse> result = mapService.searchByKeywordInRedis(keyword);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, result));
    }

    @GetMapping("/region/{areaCode}")
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getByAreaCodeOnly(
            @PathVariable Integer areaCode) {
        List<AttractionSimpleResponse> result = mapService.getByRegionFromRedis(areaCode, null);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, result));
    }

    @GetMapping("/region/{areaCode}/{siGunGuCode}")
    public ResponseEntity<ApiResponse<List<AttractionSimpleResponse>>> getByAreaCodeAndSiGunGu(
            @PathVariable Integer areaCode,
            @PathVariable Integer siGunGuCode) {
        List<AttractionSimpleResponse> result = mapService.getByRegionFromRedis(areaCode, siGunGuCode);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, result));
    }
}
