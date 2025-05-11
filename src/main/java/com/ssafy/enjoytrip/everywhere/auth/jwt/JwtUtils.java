package com.ssafy.enjoytrip.everywhere.auth.jwt;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(resolveToken(token)).getBody().getSubject();
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new ApiException(ErrorCode.TOKEN_INVALID);
        }

        return bearerToken.startsWith(JwtConstants.TOKEN_PREFIX.value()) ? bearerToken.substring(7) : bearerToken;
    }

}