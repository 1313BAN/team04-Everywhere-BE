package com.ssafy.enjoytrip.everywhere.route.repository;

import com.ssafy.enjoytrip.everywhere.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByUserId(String userId);
}