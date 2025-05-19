package com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.service;

import com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.entity.JwtBlacklist;
import com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.repository.JwtBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private final JwtBlacklistRepository blacklistRepository;

    public void blacklist(String token, Date expiresAt) {
        LocalDateTime expiry = expiresAt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        blacklistRepository.save(new JwtBlacklist(token, expiry));
    }

    public boolean isBlacklisted(String token) {
        return blacklistRepository.existsByToken(token);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistRepository.deleteAllByExpiresAtBefore(now);
    }
}
