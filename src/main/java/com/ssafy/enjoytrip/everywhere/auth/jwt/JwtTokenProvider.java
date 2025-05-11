package com.ssafy.enjoytrip.everywhere.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.auth.security.SecurityConstants;
import com.ssafy.enjoytrip.everywhere.user.domain.User;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final KeyManager keyManager;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = keyManager.getKey();
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