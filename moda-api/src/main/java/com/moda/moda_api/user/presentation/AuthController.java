package com.moda.moda_api.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.user.application.EmailService;
import com.moda.moda_api.user.application.UserService;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.presentation.request.EmailRequest;
import com.moda.moda_api.user.presentation.request.EmailVerifyRequest;
import com.moda.moda_api.user.presentation.request.PasswordVerifyRequest;
import com.moda.moda_api.user.presentation.request.UserNameRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final EmailService emailService;
	private final UserService userService;

	@PostMapping("/email/send")
	public ResponseEntity<Boolean> sendEmail(@RequestBody EmailRequest request) {
		return ResponseEntity.ok(emailService.sendVerificationCode(request.getEmail()));
	}

	@PostMapping("/email/verify")
	public ResponseEntity<Boolean> verifyEmail(@RequestBody EmailVerifyRequest request) {
		return ResponseEntity.ok(
			emailService.verifyCode(request.getEmail(), request.getCode())
		);
	}

	@PostMapping("/find-user-id")
	public ResponseEntity<String> findUserId(@RequestBody EmailVerifyRequest request){

		if(!emailService.verifyCode(request.getEmail(), request.getCode())){
			return ResponseEntity.badRequest().body("잘못된 입력입니다");
		}
		User user = userService.findByUserName(request.getEmail());
		return ResponseEntity.ok(user.getUserName());
	}

	@PostMapping("/password-change-check")
	public ResponseEntity<Boolean> checkPasswordChangePossible(@RequestBody PasswordVerifyRequest passwordVerifyRequest) {
		// 인증 코드 검증
		if (!emailService.verifyCode(passwordVerifyRequest.getEmail(), passwordVerifyRequest.getCode())) {
			return ResponseEntity.badRequest().body(false);
		}

		// 해당 이메일로 user 찾기
		User user = userService.findByUserName(passwordVerifyRequest.getEmail());

		//request의 user의 로그인 아이디와 실제 로그인 userId가 같은지 비교한다.
		if (!user.getUserName().equals(passwordVerifyRequest.getUserId())) {
			return ResponseEntity.badRequest().body(false);
		}

		return ResponseEntity.ok(true);
	}

	@PostMapping("/check-user-name")
	public ResponseEntity<Boolean> checkUserName(@RequestBody UserNameRequest userName) {
		boolean isAvailable = userService.isUserNameAvailable(userName.getUserId());
		return ResponseEntity.ok(isAvailable);
	}


}

