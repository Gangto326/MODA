package com.moda.moda_api.card.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlCache {
	String urlHash;
	String originalUrl;
	String cachedTitle;
	String cachedContext;
	LocalDateTime createdAt;

	public static UrlCache create(String originalUrl, String title, String content) {
		return new UrlCache(
			generateHash(originalUrl),
			originalUrl,
			title,
			content,
			LocalDateTime.now()
		);
	}

	public static String generateHash(String url) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(url.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 알고리즘을 수행하지 못하였습니다. ", e);
		}
	}

}
