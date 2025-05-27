package com.ssafy.enjoytrip.everywhere.map.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ApiResponse<AttractionsResponse> getAllAttractions() {
        AttractionsResponse attractions = mapService.getAllAttractions();
        return ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, attractions);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<AttractionsResponse>> searchByKeyword(@PathVariable("keyword") String keyword) {
        AttractionsResponse attractions = mapService.searchByKeyword(keyword);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, attractions));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<AttractionsResponse>> getByCategory(@PathVariable("category") String category) {
        AttractionsResponse attractions = mapService.getAttractionsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_BY_CATEGORY, attractions));
    }

    @GetMapping(params = {"level", "swLatLng", "neLatLng"})
    public ResponseEntity<ApiResponse<AttractionsResponse>> getAttractionsInBounds(@RequestParam("level") String level,
                                                                                   @RequestParam("swLatLng") String swLatLng,
                                                                                   @RequestParam("neLatLng") String neLatLng) {
        AttractionsResponse attractions = mapService.getAttractionsByBoundsWithZoom(swLatLng, neLatLng, Integer.parseInt(level));
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_IN_BOUNDS, attractions));
    }

    @GetMapping(params = {"swLatLng", "neLatLng"})
    public ResponseEntity<ApiResponse<AttractionsResponse>> getAttractionsInBounds(@RequestParam("swLatLng") String swLatLng,
                                                                                   @RequestParam("neLatLng") String neLatLng) {
        AttractionsResponse attractions = mapService.getAttractionsByBounds(swLatLng, neLatLng);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_IN_BOUNDS, attractions));
    }

}