package com.ssafy.enjoytrip.everywhere.auth.jwt;

import com.ssafy.enjoytrip.everywhere.auth.security.SecurityConstants;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.InvalidTokenException;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.ACCESS_TOKEN_EXPIRY);

        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(SecurityConstants.HEADER_AUTH);
        if (bearer != null && bearer.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearer.substring(SecurityConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public void validateTokenOrThrow(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(ErrorCode.TOKEN_INVALID);
        }
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.REFRESH_TOKEN_EXPIRY);

        return Jwts.builder()
                .setSubject(user.getUserId())  // 또는 user.getId().toString()도 가능
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}