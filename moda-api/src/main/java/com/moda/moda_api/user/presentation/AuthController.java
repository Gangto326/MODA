package com.moda.moda_api.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.user.application.EmailService;
import com.moda.moda_api.user.presentation.request.EmailRequest;
import com.moda.moda_api.user.presentation.request.EmailVerifyRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final EmailService emailService;

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
}