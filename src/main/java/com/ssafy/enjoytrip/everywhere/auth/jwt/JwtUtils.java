package com.ssafy.enjoytrip.everywhere.auth.jwt;

import java.security.Key;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class JwtUtils {

	private final KeyManager keyManager;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = keyManager.getKey();
	}

	public String getUserId(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

}