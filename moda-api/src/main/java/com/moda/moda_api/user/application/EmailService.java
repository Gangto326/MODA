package com.moda.moda_api.user.application;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final RedisTemplate<String, String> emailRedisTemplate; // 이름 변경
	private final JavaMailSender emailSender;

	public boolean sendVerificationCode(String email) {
		String code = generateCode(); // 6자리 랜덤 코드

		System.out.println(code);
		// Redis에 저장 (5분 유효)
		emailRedisTemplate.opsForValue()
			.set("EMAIL:" + email, code, 10, TimeUnit.MINUTES);

		// 이메일 발송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("인증 코드");
		message.setText("인증 코드: " + code);
		emailSender.send(message);

		return true;
	}

	public boolean verifyCode(String email, String code) {
		String savedCode = emailRedisTemplate.opsForValue()
			.get("EMAIL:" + email);
		return code.equals(savedCode);
	}



	private String generateCode() {
		return String.format("%06d", new Random().nextInt(1000000));
	}
}