package com.ssafy.enjoytrip.everywhere.map.controller;

import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
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

    @GetMapping("/{contentTypeId}")
    public ResponseEntity<ApiResponse<AttractionsResponse>> getByContentTypeId(@PathVariable("contentTypeId") Integer contentTypeId) {
        AttractionsResponse attractions = mapService.getAttractionsByContentType(contentTypeId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_BY_TYPE, attractions));
    }

    @GetMapping(params = {"level", "swLatLng", "neLatLng"})
    public ResponseEntity<ApiResponse<AttractionsResponse>> getAttractionsInBounds(@RequestParam("level") String level,
                                                                                   @RequestParam("swLatLng") String swLatLng,
                                                                                   @RequestParam("neLatLng") String neLatLng) {
        AttractionsResponse attractions = mapService.getAttractionsByBoundsWithZoom(swLatLng, neLatLng, Integer.parseInt(level));
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_IN_BOUNDS, attractions));
    }

}