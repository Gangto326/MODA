package com.moda.moda_api.common.infrastructure.resolver;

import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.common.util.HeaderUtil;
import com.moda.moda_api.common.util.JwtUtil;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = HeaderUtil.getAccessToken(request);

        // 필터에서 이미 검증했으므로, 여기서는 단순히 userId만 추출
        return jwtUtil.getUserId(token, "AccessToken");
        // return "1ef4bd1b-a842-4046-bc75-ba7b45cda1f2";
    }
}
