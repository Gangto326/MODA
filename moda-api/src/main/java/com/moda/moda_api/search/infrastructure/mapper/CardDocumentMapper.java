package com.moda.moda_api.search.infrastructure.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;

@Component
public class CardDocumentMapper {

	public CardDocumentEntity toEntity(Card card) {
		return CardDocumentEntity.builder()
			.userId(card.getUserId().getValue())
			.id(card.getCardId().getValue())
			.categoryId(card.getCategoryId().getValue())
			.typeId(card.getTypeId())
			.title(card.getTitle())
			.content(card.getContent())
			.thumbnailContent(card.getThumbnailContent())
			.thumbnailUrl(card.getThumbnailUrl())
			.titleCompletion(card.getTitle())
			.contentCompletion(card.getContent())
			.keywords(card.getKeywords())
			.embedding(card.getEmbedding().getValues())
			.createdAt(card.getCreatedAt())
			.build();
	}

	public CardDocument toDomain(CardDocumentEntity entity) {
		return CardDocument.builder()
			.cardId(new CardId(entity.getId()))
			.categoryId(new CategoryId(entity.getCategoryId()))
			.typeId(entity.getTypeId())
			.title(entity.getTitle())
			.content(entity.getContent())
			.thumbnailContent(entity.getThumbnailContent())
			.thumbnailUrl(entity.getThumbnailUrl())
			.keywords(entity.getKeywords())
			.score(entity.getScore())
			.build();
	}

	public List<CardDocumentEntity> toEntity(List<Card> cards) {
		return cards.stream()
			.map(this::toEntity)
			.collect(Collectors.toList());
	}

	public List<CardDocument> toDomain(Iterable<CardDocumentEntity> entities) {
		return StreamSupport.stream(entities.spliterator(), false)
			.map(this::toDomain)
			.collect(Collectors.toList());
	}
}
