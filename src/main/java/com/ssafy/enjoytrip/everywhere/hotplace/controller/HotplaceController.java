package com.ssafy.enjoytrip.everywhere.hotplace.controller;

import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.hotplace.service.HotplaceService;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotplace")
@RequiredArgsConstructor
public class HotplaceController {

    private final Integer TOP_N = 10;
    private final HotplaceService hotplaceService;
    private final MapRedisService mapService;
    private final RedisTemplate<String, String> userRedisTemplate;


    @PostMapping("/{attractionId}")
    public ResponseEntity<Void> addHotplace(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long attractionId) {
        String userId = userDetails.getUsername();

        boolean alreadyExists = hotplaceService.isHotplaceExists(userId, attractionId);
        if (!alreadyExists) {
            hotplaceService.addHotplace(userId, attractionId);
            hotplaceService.increaseScore(attractionId);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{attractionId}")
    public ResponseEntity<Void> removeHotplace(@PathVariable Long attractionId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        hotplaceService.removeHotplace(userId, attractionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AttractionSimpleResponse>> getHotplaces(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(hotplaceService.getHotplaces(userId));
    }

    @GetMapping("/top")
    public ResponseEntity<List<AttractionSimpleResponse>> getTopHotplaces() {
        List<Long> topIds = hotplaceService.getTopHotplaces(TOP_N);
        List<AttractionSimpleResponse> responses = mapService.getByContentIds(topIds);
        return ResponseEntity.ok(responses); // ✅ 이미 DTO이므로 map 필요 없음
    }

    @GetMapping("/{attractionId}/check")
    public ResponseEntity<Boolean> checkHotplace(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long attractionId) {

        String userId = userDetails.getUsername();
        boolean exists = Boolean.TRUE.equals(userRedisTemplate.opsForSet().isMember("hotplace:" + userId, attractionId.toString()));
        return ResponseEntity.ok(exists);
    }

<<<<<<< Updated upstream
=======
    /**
     * 사용자가 찜한 최근 5개 장소를 이용한 장소 추천
     */
    @GetMapping("/personal")
    public ResponseEntity<List<AttractionSimpleResponse>> getHotplaceBasedRecommendations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();

        List<AttractionSimpleResponse> hotplaces = hotplaceService.getHotplaces(userId);
        List<String> keywords = hotplaces.stream()
                .map(AttractionSimpleResponse::title)
                .limit(5)  // 최근 찜한 5개로 제한
                .toList();

        List<AttractionSimpleResponse> result = mapService.searchByKeywordEmbedding(keywords.toString());
        return ResponseEntity.ok(result);
    }

>>>>>>> Stashed changes
}
