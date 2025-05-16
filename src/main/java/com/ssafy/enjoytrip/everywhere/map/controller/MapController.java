package com.ssafy.enjoytrip.everywhere.map.controller;

import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ApiResponse<AttractionsResponse> getAllAttractions() {
        AttractionsResponse dto = new AttractionsResponse(mapService.getAllAttractions());
        return ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS, dto);
    }

    @GetMapping("/{contentTypeId}")
    public ApiResponse<AttractionsResponse> getAttractionsByType(@PathVariable Integer contentTypeId) {
        AttractionsResponse dto = new AttractionsResponse(mapService.getAttractionsByType(contentTypeId));
        return ApiResponse.success(SuccessCode.SUCCESS_GET_ATTRACTIONS_BY_TYPE, dto);
    }
}
