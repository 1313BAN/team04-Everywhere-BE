package com.ssafy.enjoytrip.everywhere.route.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.enjoytrip.everywhere.chat.service.ChatService;
import com.ssafy.enjoytrip.everywhere.map.dto.request.NearAttractionRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.service.MapRedisService;
import com.ssafy.enjoytrip.everywhere.route.dto.request.RouteRequest;
import com.ssafy.enjoytrip.everywhere.route.dto.request.RouteSimpleRequest;
import com.ssafy.enjoytrip.everywhere.route.entity.Route;
import com.ssafy.enjoytrip.everywhere.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final ChatService chatService;
    private final MapRedisService mapService;


    public RouteRequest createRoute(String userId, RouteRequest request) {
        String locationSummary = createLocationSummary(request.attractions());
        String category = mapService.getByContentId(request.attractions().getFirst().contentId()).category();
        // 저장
//        Route route = Route.builder()
//                .userId(userId) // 실제 구현 시 SecurityContext 등에서 가져오기
//                .title(request.title())
//                .overview(request.description())
//                .category(category)
//                .location(locationSummary)
//                .distance("총 거리 계산 예정")
//                .build();
//        routeRepository.save(route);

        // 경로

        // 근처 식당 찾기
        List<AttractionSimpleResponse> nearRestaurants = mapService.findNearByLocationAndCategory(
                new NearAttractionRequest(
                        request.latitude(),
                        request.longitude(),
                        1000,
                        "숙박"));
        System.out.println("nearRestaurants: " + nearRestaurants);
        List<RouteSimpleRequest> restaurant = mapper(nearRestaurants);
        String restaurantJoined = restaurant.stream()
                .map(RouteSimpleRequest::toString)
                .collect(Collectors.joining(", "));

        String answer = chatService.routeResponse(userId, request.attractions().toString(), restaurantJoined);
        System.out.println("answer: " + answer);

        List<RouteSimpleRequest> newRoute = new ArrayList<>();
        newRoute = putRouteData(answer);

        if (newRoute == null) {return null;}

        // 이후 route_attraction 테이블 저장 로직 추가 가능
        return new RouteRequest(
                request.title(),
                request.description(),
                request.longitude(),
                request.latitude(),
                newRoute
        );
    }

    private List<RouteSimpleRequest> mapper(List<AttractionSimpleResponse> nearRestaurants) {
        if (nearRestaurants == null || nearRestaurants.isEmpty()) {
            return null;
        }

        List<RouteSimpleRequest> routeList = new ArrayList<>();

        int sequence = 1;
        for (AttractionSimpleResponse res : nearRestaurants) {
            RouteSimpleRequest route = new RouteSimpleRequest(
                    res.contentId(),
                    res.title(),
                    res.contentTypeId(),
                    res.latitude(),
                    res.longitude(),
                    sequence++
            );
            routeList.add(route);
        }
        return routeList;
    }

    private String createLocationSummary(List<RouteSimpleRequest> routes) {
        return routes.stream()
                .map(a -> a.latitude() + "," + a.longitude())
                .reduce((a, b) -> a + " -> " + b)
                .orElse("");
    }

    private List<RouteSimpleRequest> putRouteData(String answer) {
        // if answer를 리스트로 바꿀 수 없으면 null 반환
        if (answer == null || answer.isBlank()) {
            return null;
        }

        try {
            // 문자열에서 대괄호 제거 후 쉼표로 분할
            String cleaned = answer.replaceAll("\\[|\\]", "");
            String[] parts = cleaned.split(",");

            List<RouteSimpleRequest> result = new ArrayList<>();

            int sequence = 1;
            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.isEmpty()) continue;

                long contentId = Long.parseLong(trimmed);
                // AttractionSimpleResponse에서 필요한 정보 추출
                AttractionSimpleResponse data = mapService.getByContentId(contentId);
                if (data == null) continue;

                RouteSimpleRequest route = new RouteSimpleRequest(
                        data.contentId(),
                        data.title(),
                        data.contentTypeId(),
                        data.latitude(),
                        data.longitude(),
                        sequence++
                );
                result.add(route);
            }

            return result;
        } catch (Exception e) {
            // 파싱 실패 시 null 반환
            return null;
        }
    }

    private String createCategory() {

        return "";
    }

}

