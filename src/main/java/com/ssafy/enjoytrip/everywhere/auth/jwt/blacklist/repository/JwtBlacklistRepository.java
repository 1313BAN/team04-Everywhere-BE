package com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.repository;

import com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.entity.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    boolean existsByToken(String token);
    void deleteAllByExpiresAtBefore(LocalDateTime time);
}

