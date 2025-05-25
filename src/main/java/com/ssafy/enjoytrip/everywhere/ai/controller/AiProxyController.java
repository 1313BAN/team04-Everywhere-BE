package com.ssafy.enjoytrip.everywhere.ai.controller;


import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.history.dto.request.SearchRequest;
import com.ssafy.enjoytrip.everywhere.history.service.HistoryService;
import com.ssafy.enjoytrip.everywhere.hotplace.service.HotplaceService;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * @param userDetails
     * @return
     */
    @GetMapping("/keywords")
    public ResponseEntity<List<Long>> getKeywordBasedRecommendations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String userId = userDetails.getUsername();
        List<String> keywords = historyService.getRecentKeywords(userId);
        // todo: 위 키워드로 쿼리 생성

        SearchRequest request = new SearchRequest(keywords);
        List<Long> recommended = webClient.post()
                .uri("/api/ai/keywords")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .block();

        return ResponseEntity.ok(recommended);
    }

    @GetMapping("/hotplaces")
    public ResponseEntity<List<Long>> getHotplaceBasedRecommendations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String userId = userDetails.getUsername();

        // ✅ 사용자의 찜 목록 (title 기반 키워드 추출)
        List<AttractionSimpleResponse> hotplaces = hotplaceService.getHotplaces(userId);
        List<String> keywords = hotplaces.stream()
                .map(AttractionSimpleResponse::title)
                .limit(5)  // 최근 찜한 5개로 제한
                .collect(Collectors.toList());

        SearchRequest request = new SearchRequest(keywords);
        System.out.println("hotplaces: " + request.toString());

        List<Long> recommended = webClient.post()
                .uri("/api/ai/hotplaces")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .block();

        return ResponseEntity.ok(recommended);
    }
}


