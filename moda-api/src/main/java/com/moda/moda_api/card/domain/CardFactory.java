package com.moda.moda_api.card.domain;

import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.user.domain.UserId;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CardFactory {
	public Card create(UserId userId, CategoryId categoryId,
		Integer typeId, String urlHash,
		String title, String content, String thumbnailContent, String thumbnailUrl,
		EmbeddingVector embedding, String[] keywords, String[] subContent) {
		return Card.builder()
			.cardId(generateCardId())
			.userId(userId)
			.categoryId(categoryId)
			.typeId(typeId)
			.urlHash(urlHash)
			.title(title)
			.content(content)
			.thumbnailContent(thumbnailContent)
			.thumbnailUrl(thumbnailUrl)
			.viewCount(0)
			.embedding(embedding)
			.createdAt(LocalDateTime.now())
			.keywords(keywords)
			.subContents(subContent)
			.build();
	}
	public Card createFromImage(UserId userId,String imageUrl,String urlHash , AIAnalysisResponseDTO aiAnalysis) {
		return Card.builder()
			.userId(userId)
			.cardId(generateCardId())
			.categoryId(aiAnalysis.getCategoryId())
			.typeId(CardContentType.IMG.getTypeId())
			.title("ImageTitle")
			.keywords(aiAnalysis.getKeywords())
			.thumbnailContent("Image ThumbNail Content")
			.embedding(aiAnalysis.getEmbeddingVector())
			.content(imageUrl)
			.thumbnailUrl(imageUrl)
			.urlHash(urlHash)
			.createdAt(LocalDateTime.now())
			.build();
	}

	// Card의 ID를 생성. common의 UUID 메서드 사용 예정
	private CardId generateCardId() {
		return new CardId(UUID.randomUUID().toString());
	}
}
