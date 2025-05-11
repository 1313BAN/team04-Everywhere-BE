package com.ssafy.enjoytrip.everywhere.auth.jwt;

import java.security.Key;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils {

	private final KeyManager keyManager;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = keyManager.getKey();
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