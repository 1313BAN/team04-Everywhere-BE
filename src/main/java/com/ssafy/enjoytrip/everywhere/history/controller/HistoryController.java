package com.ssafy.enjoytrip.everywhere.history.controller;

import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.history.service.HistoryService;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping
    public ResponseEntity<Void> addKeyword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody Map<String, String> body) {
        String userId = userDetails.getUsername();
        String keyword = body.get("keyword");
        historyService.addKeyword(userId, keyword);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getRecentKeywords(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(historyService.getRecentKeywords(userId));
    }

    @GetMapping("/personal")
    public ResponseEntity<List<AttractionSimpleResponse>> historyFitAttraction(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(historyService.historyFitAttraction(userId));
    }
}

