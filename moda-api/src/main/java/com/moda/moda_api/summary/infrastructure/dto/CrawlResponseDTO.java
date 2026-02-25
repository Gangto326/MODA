package com.moda.moda_api.summary.infrastructure.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CrawlResponseDTO {

	private CategoryId categoryId;
	private String[] keywords;
	private String thumbnailContent;
	private String content;
	private EmbeddingVector embeddingVector;
	private String title;
	private String thumbnailUrl;
	private String[] images;
	private String domainType;

	@Builder
	public CrawlResponseDTO(
		@JsonProperty("category_id") CategoryId categoryId,
		@JsonProperty("keywords") String[] keywords,
		@JsonProperty("thumbnail_content") String thumbnailContent,
		@JsonProperty("content") String content,
		@JsonProperty("embedding_vector") EmbeddingVector embeddingVector,
		@JsonProperty("title") String title,
		@JsonProperty("thumbnail_url") String thumbnailUrl,
		@JsonProperty("images") String[] images,
		@JsonProperty("domain_type") String domainType) {

		this.categoryId = categoryId;
		this.keywords = Objects.requireNonNullElse(keywords, new String[0]);
		this.thumbnailContent = Objects.requireNonNullElse(thumbnailContent, "");
		this.content = Objects.requireNonNullElse(content, "");
		this.embeddingVector = Objects.requireNonNullElse(embeddingVector, new EmbeddingVector(null));
		this.title = Objects.requireNonNullElse(title, "");
		this.thumbnailUrl = thumbnailUrl;
		this.images = Objects.requireNonNullElse(images, new String[0]);
		this.domainType = Objects.requireNonNullElse(domainType, "UNCLASSIFIED");
	}
}
