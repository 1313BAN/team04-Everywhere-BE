package com.ssafy.enjoytrip.everywhere.auth.jwt;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.service.JwtBlacklistService;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.enjoytrip.everywhere.auth.config.PublicPath;
import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenResolver tokenResolver;
	private final CustomUserDetailsService userDetailsService;
	private final JwtUtils jwtUtils;
	private final JwtBlacklistService blacklistService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)
			throws ServletException, IOException {
		String bearer = request.getHeader(JwtConstants.AUTH_HEADER.value());
		String token  = tokenResolver.resolve(bearer);

		if (isBlacklistedToken(response, token)) return;

		authenticateToken(request, token);
		chain.doFilter(request, response);
	}


	private void authenticateToken(HttpServletRequest request, String token) {
		String userId = jwtUtils.getUserId(token);
		var userDetails = userDetailsService.loadUserByUsername(userId);
		var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private boolean isBlacklistedToken(HttpServletResponse response, String token) throws IOException {
		if (token != null && blacklistService.isBlacklisted(token)) {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			ApiResponse<?> errorResponse = ApiResponse.error(ErrorCode.TOKEN_BLACKLISTED.status(), ErrorCode.TOKEN_BLACKLISTED.message());
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().write(mapper.writeValueAsString(errorResponse));
			return true;
		}
		return false;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return PublicPath.matches(request.getRequestURI());
	}
}