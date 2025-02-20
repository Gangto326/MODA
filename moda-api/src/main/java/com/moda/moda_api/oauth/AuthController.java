package com.moda.moda_api.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.user.application.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

// @RestController
// @RequestMapping("/api")
// public class OAuthController {

	// private final UserService userService;
	// private final GoogleService googleService;
	//
	// public OAuthController(KakaoService kakaoService, UserService userService, JwtTokenProvider jwtTokenProvider,
	// 	GoogleService googleService) {
	// 	this.kakaoService = kakaoService;
	// 	this.userService = userService;
	// 	this.jwtTokenProvider = jwtTokenProvider;
	// 	this.googleService = googleService;
	// }
	//
	// @GetMapping("/oauth/google")
	// @Operation(summary = "구글 로그인 URL 요청", description = "구글 OAuth 로그인 URL을 반환합니다.")
	// public ResponseEntity<String> getGoogleLoginUrl() {
	// 	System.out.println("요처ㅗㅇ ");
	// 	String googleAuthUrl = googleService.getGoogleLoginUrl();
	// 	return ResponseEntity.ok(googleAuthUrl);
	// }
	//
	// @GetMapping("/oauth/kakao")
	// @Operation(summary = "카카오 로그인 URL 요청", description = "카카오 OAuth 로그인 URL을 반환합니다.")
	// public ResponseEntity<String> getKakaoLoginUrl() {
	// 	String kakaoAuthUrl = kakaoService.getKakaoLoginUrl();
	// 	return ResponseEntity.ok(kakaoAuthUrl);
	// }
	//
	// @GetMapping("/login/oauth2/code/google")
	// @Operation(summary = "구글 로그인 처리", description = "구글 OAuth 로그인 후 토큰을 생성하고 리다이렉트합니다.")
	// public RedirectView googleLogin(
	// 	@RequestParam @Parameter(description = "구글에서 제공하는 인증 코드") String code,
	// 	HttpServletResponse response) {
	// 	String accessToken = googleService.getAccessToken(code);
	// 	Map<String, Object> userInfo = googleService.getUserInfo(accessToken);
	//
	// 	String email = (String) userInfo.get("email");
	// 	String name = (String) userInfo.get("name");
	//
	// 	String userSeq = checkRegisteredUser(email, name);
	// 	String jwtAccessToken = jwtTokenProvider.generateToken(userSeq, name, TokenType.ACCESS);
	// 	String jwtRefreshToken = jwtTokenProvider.generateToken(userSeq, name, TokenType.REFRESH);
	// 	response.addCookie(storeRefreshToken(jwtRefreshToken));
	//
	// 	RedirectView redirectView = new RedirectView("http://localhost:5173/oauth/google/success");
	// 	redirectView.addStaticAttribute("token", jwtAccessToken);
	// 	redirectView.addStaticAttribute("username", name);
	// 	return redirectView;
	// }

	// private String checkRegisteredUser(String email, String name) {
	// 	String userId = email != null && email.contains("@") ? email.split("@")[0] : null;
	// 	String userSeq = userService.findUserByUserId(userId);
	//
	// 	if (userSeq == null || userSeq.equals("")) {
	// 		User user = new User();
	// 		user.setId(userId);
	// 		user.setEmail(email);
	// 		user.setPassword(UUID.randomUUID().toString());
	// 		user.setName(name);
	// 		userService.registUser(user);
	// 		userSeq = user.getUserSeq();
	// 	}
	// 	return userSeq;
	// }
	//
	// private Cookie storeRefreshToken(String jwtRefreshToken) {
	// 	Cookie refreshTokenCookie = new Cookie("refreshToken", jwtRefreshToken);
	// 	refreshTokenCookie.setHttpOnly(true);
	// 	refreshTokenCookie.setSecure(false); // HTTPS 사용 시 true
	// 	refreshTokenCookie.setPath("/");
	// 	refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
	// 	return refreshTokenCookie;
	// }
// }