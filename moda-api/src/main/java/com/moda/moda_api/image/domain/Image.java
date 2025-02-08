package com.moda.moda_api.image.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.exception.UnauthorizedException;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
	private ImageId imageId;
	private UserId userId;
	private String imageUrl;
	private CategoryId categoryId;
	private EmbeddingVector embedding;
	private String[] keywords;
	private Integer viewCount;
	private LocalDateTime createdAt;

	// 🔹 빌더 사용 시 기본값 설정
	public static ImageBuilder builder() {
		return new ImageBuilder()
			.imageId(new ImageId(UUID.randomUUID().toString())) // 기본값 설정
			.viewCount(0)
			.createdAt(LocalDateTime.now());
	}

	public void validateOwnership(UserId userId) {
		if (!this.userId.equals(userId)) {
			throw new UnauthorizedException("권한이 존재하지 않습니다.");
		}
	}
}
