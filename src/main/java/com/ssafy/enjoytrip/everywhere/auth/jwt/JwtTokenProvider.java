package com.ssafy.enjoytrip.everywhere.auth.jwt;

import com.ssafy.enjoytrip.everywhere.auth.domain.User;
import com.ssafy.enjoytrip.everywhere.auth.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public JwtToken generateToken(User user) {
        return JwtToken.builder()
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.ACCESS_TOKEN_EXPIRY);

        return Jwts.builder()
                .setSubject(user.userId())
                .claim(JwtConstants.CLAIM_TYPE.value(), JwtConstants.TOKEN_TYPE_ACCESS.value())
                .claim(JwtConstants.CLAIM_NAME.value(), user.nickname())
                .claim(JwtConstants.CLAIM_ROLE.value(), user.role().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.REFRESH_TOKEN_EXPIRY);

        return Jwts.builder()
                .setSubject(user.userId())
                .claim(JwtConstants.CLAIM_TYPE.value(), JwtConstants.TOKEN_TYPE_REFRESH.value())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }
}