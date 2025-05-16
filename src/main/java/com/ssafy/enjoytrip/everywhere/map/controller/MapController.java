package com.ssafy.enjoytrip.everywhere.map.controller;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/attractions")
    public List<AttractionSimpleResponse> getAllAttractions() {
        return mapService.getAllAttractions();
    }

    @GetMapping("/{contentTypeId}")
    public List<AttractionSimpleResponse> getAttractionsByType(@PathVariable Integer contentTypeId) {
        return mapService.getAttractionsByType(contentTypeId);
    }
}
