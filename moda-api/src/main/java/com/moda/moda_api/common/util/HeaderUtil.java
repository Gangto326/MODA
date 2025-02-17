package com.moda.moda_api.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HeaderUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_COOKIE = "RefreshToken";
    private static final String TOKEN_PREFIX = "Bearer ";

    public static String getAuthorizationHeaderName() {
        return AUTHORIZATION_HEADER;
    }

    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public static String getRefreshCookieName() {
        return REFRESH_COOKIE;
    }

    public static String getAccessToken(HttpServletRequest httpServletRequest) {

        String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if(authorization != null && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
    public static String getRefreshToken(HttpServletRequest httpServletRequest) {
        // 먼저 쿠키에서 확인
        Cookie[] cookieList = httpServletRequest.getCookies();

        if(cookieList != null) {
            for(Cookie cookie: cookieList) {
                if(cookie.getName().equals(REFRESH_COOKIE)) {
                    return cookie.getValue();
                }
            }
        }

        // 쿠키에 없다면 헤더에서 확인
        String cookieHeader = httpServletRequest.getHeader("Cookie");
        if(cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for(String cookie : cookies) {
                String[] parts = cookie.trim().split("=");
                if(parts.length == 2 && parts[0].equals(REFRESH_COOKIE)) {
                    return parts[1];
                }
            }
        }

        return null;
    }
}