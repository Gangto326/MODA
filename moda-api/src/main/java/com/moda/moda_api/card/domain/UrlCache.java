package com.moda.moda_api.card.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

import com.moda.moda_api.category.domain.CategoryId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UrlCache {
	String urlHash;
	String originalUrl;
	Integer typeId;
	CategoryId categoryId;
	String cachedTitle;
	String cachedContent;
	String cachedThumbnailContent;
	String cachedThumbnailUrl;
	EmbeddingVector cachedEmbedding;
	String[] cachedKeywords;
	String[] cachedSubContents;

	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();

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
