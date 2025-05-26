package com.ssafy.enjoytrip.everywhere.hotplace.repository;

import java.util.List;

public interface HotplaceRepository {
    void addHotplace(String userId, Long attractionId);
    void removeHotplace(String userId, Long attractionId);
    List<Long> getHotplaceIdsByUser(String userId);

    // 인기 여행지 Top-N 조회 기능
    void increaseScore(Long attractionId);
    List<Long> getTopHotplaces(int limit);
    boolean existsHotplace(String userId, Long attractionId);
}
