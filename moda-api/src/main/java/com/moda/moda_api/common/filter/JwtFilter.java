package com.moda.moda_api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.common.infrastructure.TokenService;
import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.HeaderUtil;
import com.moda.moda_api.common.util.JwtUtil;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.user.domain.UserRepository;

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
    private final UserRepository userRepository;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePaths = {
                "/api/user/login",
                "/api/user/logout",
                "/api/user/signup",
                "/api/user/refresh",
                "/api/auth/email/send",
                "/api/auth/email/verify"
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

        if (request.getRequestURI().contains("/api/user/refresh")) {
            String refreshToken = HeaderUtil.getRefreshToken(request);

            if (refreshToken != null) {
                // RefreshToken 유효성 검증 (JWT 검증 + DB 활성화 상태 확인)
                if (!jwtUtil.isValidToken(refreshToken, "RefreshToken") ||
                        !tokenService.validateRefreshToken(refreshToken).isActive()) {
                    sendErrorResponse(response, "Refresh token이 유효하지 않습니다.");
                    return;
                }
            }
        }
        else {
            String accessToken = HeaderUtil.getAccessToken(request);

            if (accessToken != null && !jwtUtil.isValidToken(accessToken, "AccessToken")){
                String refreshToken = HeaderUtil.getRefreshToken(request);

                if (refreshToken != null &&
                    jwtUtil.isValidToken(refreshToken, "RefreshToken") &&
                    tokenService.validateRefreshToken(refreshToken).isActive()){

                    String userId = jwtUtil.getUserId(refreshToken,"RefreshToken");
                    User user = userRepository.findById(userId).get();
                    TokenDto newAccessToken = jwtUtil.generateToken(user, "AccessToken");

                    tokenService.saveAccessToken(new UserId(userId), newAccessToken.getTokenValue());


                    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401

                    response.setHeader(
                        HeaderUtil.getAuthorizationHeaderName(),
                        HeaderUtil.getTokenPrefix() + newAccessToken.getTokenValue()
                    );

                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(
                        Map.of("message", "Access Token이 만료되었습니다. 새로운 토큰을 발급했습니다.")
                    ));

                    return;
                }
            }

            if (accessToken == null || !jwtUtil.isValidToken(accessToken, "AccessToken")) {
                sendErrorResponse(response, "Access token이 존재하지 않습니다.");
                return;
            }

            // Redis에서 AccessToken 유효성 검증
            UserId userId = new UserId(jwtUtil.getUserId(accessToken, "AccessToken"));
            if (!tokenService.validateAccessToken(userId, accessToken)) {
                sendErrorResponse(response, "Access token이 유효하지 않습니다.");
                return;
            }
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