package com.ssafy.enjoytrip.everywhere.hotplace.service;

import com.ssafy.enjoytrip.everywhere.hotplace.repository.HotplaceRepository;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotplaceService {

    private final HotplaceRepository hotplaceRepository;
    private final AttractionRepository attractionRepository;

    public void addHotplace(String userId, Long attractionId) {
        boolean alreadyAdded = hotplaceRepository.existsHotplace(userId, attractionId);
        if (!alreadyAdded) {
            hotplaceRepository.addHotplace(userId, attractionId);
            hotplaceRepository.increaseScore(attractionId); // ✅ 최초 추가일 때만 점수 증가
        }    }

    public void removeHotplace(String userId, Long attractionId) {
        hotplaceRepository.removeHotplace(userId, attractionId);
    }

    public List<AttractionSimpleResponse> getHotplaces(String userId) {
        List<Long> ids = hotplaceRepository.getHotplaceIdsByUser(userId);
        return attractionRepository.findByContentIdIn(ids)
                .stream()
                .map(AttractionSimpleResponse::from)
                .collect(Collectors.toList());
    }

    public void increaseScore(Long attractionId) {
        hotplaceRepository.increaseScore(attractionId);
    }

    public List<Long> getTopHotplaces(int limit) {
        return hotplaceRepository.getTopHotplaces(limit);
    }

    public boolean isHotplaceExists(String userId, Long attractionId) {
        return hotplaceRepository.existsHotplace(userId, attractionId);
    }
}
