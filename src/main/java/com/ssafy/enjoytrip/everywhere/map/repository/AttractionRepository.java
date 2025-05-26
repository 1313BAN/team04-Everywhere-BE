package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    List<Attraction> findByCategory(String category);

    @Query("SELECT new com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse(" +
            "a.contentId, a.title, a.contentTypeId, a.latitude, a.longitude, " +
            "a.areaCode, a.siGunGuCode, a.mapLevel, a.tel, a.address, a.firstImage, a.secondImage, a.category) " +
            "FROM Attraction a " +
            "WHERE a.latitude BETWEEN :swLat AND :neLat " +
            "AND a.longitude BETWEEN :swLng AND :neLng")
    List<AttractionSimpleResponse> findWithinBoundsWithLimit(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng,
            Pageable pageable
    );

    @Query("SELECT a FROM Attraction a " +
            "WHERE a.latitude BETWEEN :swLat AND :neLat " +
            "AND a.longitude BETWEEN :swLng AND :neLng")
    List<Attraction> findWithinBounds(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng
    );

    @Query("SELECT a FROM Attraction a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Attraction> findByTitleContaining(@Param("keyword") String keyword);

    List<Attraction> findByContentIdIn(List<Long> contentIds);
}