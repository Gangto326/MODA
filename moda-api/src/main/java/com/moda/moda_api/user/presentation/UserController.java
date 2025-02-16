package com.moda.moda_api.user.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.HeaderUtil;
import com.moda.moda_api.user.application.UserService;
import com.moda.moda_api.user.application.response.AuthResponse;
import com.moda.moda_api.user.application.response.ErrorResponse;
import com.moda.moda_api.user.application.response.SuccessResponse;
import com.moda.moda_api.user.exception.InvalidRequestException;
import com.moda.moda_api.user.exception.UserNotFoundException;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.PasswordResetRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param request 회원가입에 필요한 정보를 담은 요청 객체
     * @return 생성된 사용자의 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("회원가입이 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param request 로그인에 필요한 정보를 담은 요청 객체
     * @return 로그인된 사용자의 정보
     */
     @PostMapping("/login")
     public ResponseEntity<Boolean> login(@RequestBody LoginRequest request) {

         AuthResponse authResponse = userService.login(request);

         HttpHeaders httpHeaders = new HttpHeaders();

         httpHeaders.add(HeaderUtil.getAuthorizationHeaderName(), HeaderUtil.getTokenPrefix() + authResponse.getAccessToken());

         // RefreshToken을 HttpOnly Cookie로 전달.
         ResponseCookie responseCookie = ResponseCookie
                 .from(HeaderUtil.getRefreshCookieName(), authResponse.getRefreshToken())
                 .path("/") // 위 사이트에서 쿠키를 허용할 경로를 설정.
                 .httpOnly(true) // HTTP 통신을 위해서만 사용하도록 설정.
                 .secure(true) // Set-Cookie 설정.
                 .maxAge(authResponse.getMaxAge() / 1000) // RefreshToken과 동일한 만료 시간으로 설정.
                 .build();

         return ResponseEntity.ok()
                 .headers(httpHeaders)
                 .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                 .header("Access-Control-Expose-Headers", "Set-Cookie")
                 .body(true);
     }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        // HTTP Header의 Authorization (AccessToken) 추출.
        String accessToken = HeaderUtil.getAccessToken(request);

        System.out.println(accessToken);
        userService.logout(accessToken);


        // maxAge(0)으로 RefreshToken 삭제.
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseCookie responseCookie = ResponseCookie
                .from(HeaderUtil.getRefreshCookieName(), "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .headers(httpHeaders).header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(null);
    }



    @PatchMapping("/reset-password")
    public ResponseEntity<Boolean> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        if (!passwordResetRequest.getNewPasswordConfirm().equals(passwordResetRequest.getNewPassword())) {
            return ResponseEntity.badRequest().body(Boolean.FALSE);
        }

        try {
            boolean result = userService.resetPassword(passwordResetRequest);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException | InvalidRequestException e) {
            log.error("비밀번호 재설정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Boolean.FALSE);
        }
    }
}
