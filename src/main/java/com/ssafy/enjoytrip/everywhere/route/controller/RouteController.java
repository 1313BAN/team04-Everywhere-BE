package com.ssafy.enjoytrip.everywhere.route.controller;

import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.route.dto.request.RouteRequest;
import com.ssafy.enjoytrip.everywhere.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<Void> createRoute(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RouteRequest request) {
        String userId = userDetails.getUsername();
        routeService.createRoute(userId, request);

        return ResponseEntity.ok().build();
    }

}