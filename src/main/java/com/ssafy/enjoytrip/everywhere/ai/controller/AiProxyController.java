package com.ssafy.enjoytrip.everywhere.ai.controller;


import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationRequest;
import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.history.dto.request.SearchRequest;
import com.ssafy.enjoytrip.everywhere.history.service.HistoryService;
import com.ssafy.enjoytrip.everywhere.hotplace.service.HotplaceService;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiProxyController {

    private final HistoryService historyService;
    private final HotplaceService hotplaceService;
    private final WebClient webClient;

    /**
     * 사용자가 검색한 최근 5개 검색어를 이용한 추천
     */
    @GetMapping("/keywords")
    public ResponseEntity<List<Long>> getKeywordBasedRecommendations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String userId = userDetails.getUsername();
        List<String> keywords = historyService.getRecentKeywords(userId);

        SearchRequest request = new SearchRequest(keywords);
        List<Long> recommended = webClient.post()
                .uri("/api/ai/keywords")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .block();

        return ResponseEntity.ok(recommended);
    }

    /**
     * 사용자가 찜한 최근 5개 장소를 이용한 장소 추천
     */
    // @GetMapping("/hotplaces")
    // public ResponseEntity<List<Long>> getHotplaceBasedRecommendations(
    //         @AuthenticationPrincipal CustomUserDetails userDetails) {
    //     String userId = userDetails.getUsername();
    //
    //     // ✅ 사용자의 찜 목록 (title 기반 키워드 추출)
    //     // List<AttractionSimpleResponse> hotplaces = hotplaceService.getHotplaces(userId);
    //     // List<String> keywords = hotplaces.stream()
    //     //         .map(AttractionSimpleResponse::title)
    //     //         .limit(5)  // 최근 찜한 5개로 제한
    //     //         .collect(Collectors.toList());
    //
    //     SearchRequest request = new SearchRequest(keywords);
    //     System.out.println("hotplaces: " + request.toString());
    //
    //     List<Long> recommended = webClient.post()
    //             .uri("/api/ai/hotplaces")
    //             .bodyValue(request)
    //             .retrieve()
    //             .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
    //             .block();
    //
    //     return ResponseEntity.ok(recommended);
    // }

    /**
     * 사용자 근방 n KM 내의 장소 추천
     */
    @PostMapping("/location")
    public ResponseEntity<List<Long>> getCurrentLocation(@RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        LocationSearchRequest request = new LocationSearchRequest(latitude, longitude, 5);

        List<Long> recommended = webClient.post()
                .uri("/api/ai/location")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .block();

        return ResponseEntity.ok(recommended);
    }


}