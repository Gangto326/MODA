package com.moda.moda_api.user.application;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.moda.moda_api.user.exception.EmailException;
import com.moda.moda_api.user.exception.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private static final String SENDER_EMAIL = "moda@gmail.com";

	private final RedisTemplate<String, String> emailRedisTemplate; // 이름 변경
	private final JavaMailSender emailSender;

	public Boolean sendVerificationCode(String email) {
		String code = generateCode();

		emailRedisTemplate.opsForValue()
			.set("EMAIL:VERIFY:" + email, code, 10, TimeUnit.MINUTES);

		String subject = "[MODA] 인증번호 발급";
		String content = createVerificationEmailContent(code);

		return sendHtmlEmail(email, subject, content);
	}

	private boolean sendHtmlEmail(String to, String subject, String htmlContent) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(SENDER_EMAIL);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			emailSender.send(message);
			return true;
		} catch (MessagingException e) {
			throw new EmailException("이메일 전송 실패", e);
		}
	}

	private String createVerificationEmailContent(String code) {
		return String.format("""
            <h3>안녕하세요. MODA입니다.</h3>
            인증번호가 발급되었습니다.
            인증번호: %s
            해당 인증번호를 MODA 사이트에 입력하여 인증하여 주시기 바랍니다.
            감사합니다.
            PLANe 팀
            """, code);
	}

	public boolean verifyCode(String email, String code) {
		String savedCode = emailRedisTemplate.opsForValue()
			.get("EMAIL:VERIFY:" + email);
		return code.equals(savedCode);
	}

	private String generateCode() {
		return String.format("%06d", new Random().nextInt(1000000));
	}
}