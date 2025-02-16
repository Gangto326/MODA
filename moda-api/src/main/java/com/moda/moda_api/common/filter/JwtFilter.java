package com.moda.moda_api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.common.infrastructure.TokenService;
import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.HeaderUtil;
import com.moda.moda_api.common.util.JwtUtil;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;

import jakarta.mail.Header;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final TokenService tokenService;
	private final ObjectMapper objectMapper;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String[] excludePaths = {
			"/api/user/login",
			"/api/user/logout",
			"/api/user/signup",
			"/api/user/refresh",
			"/api/auth/email/send",
			"/api/auth/email/verify",
			"/api/auth/find-user-id",
			"/api/auth/check-user-name",
			"/api/user/refresh",
			"/swagger-ui",  // 수정
			"/v3/api-docs",  // 수정
			"/swagger-resources",
			"/webjars",
			"/api/user/check-user-name",
			"/api/auth/password-change-check"

		};

		String path = request.getRequestURI();
		return Arrays.stream(excludePaths)
			.anyMatch(path::startsWith);
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = HeaderUtil.getAccessToken(request);

		// 만약 해당 값이 없으면 Access Token이 존재하지 않은 것
		if (accessToken == null) {
			sendErrorResponse(response, "Access token이 존재하지 않습니다");
			return;
		}

		//JWT 유효성 검증
		if (!jwtUtil.isValidToken(accessToken, "AccessToken")) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");
			response.getWriter().write(objectMapper.writeValueAsString(
				Map.of(
					"message", "Access Token이 만료되었습니다.",
					"code", "ACCESS_TOKEN_EXPIRED"
				)
			));
			return;
		}

		// 2. Redis 토큰 유효성 검증 (로그아웃 여부 등 체크)
		UserId userId = new UserId(jwtUtil.getUserId(accessToken, "AccessToken"));
		if (!tokenService.validateAccessToken(userId, accessToken)) {
			sendErrorResponse(response, "Access token이 유효하지 않습니다. ");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.getWriter().write(objectMapper.writeValueAsString(
			Map.of("message", message)
		));
	}
}