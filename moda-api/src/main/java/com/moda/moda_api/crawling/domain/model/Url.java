package com.moda.moda_api.crawling.domain.model;

import com.moda.moda_api.card.domain.CardContentType;
import com.moda.moda_api.crawling.domain.service.CardContentTypeResolver;
import com.moda.moda_api.crawling.domain.service.UrlContentTypeResolver;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Url {
	private String value;
	private UrlDomainType urlType;
	private CardContentType cardContentType;

	public Url(String value) {
		this.value = value;
		this.urlType = UrlContentTypeResolver.resolve(value);
		this.cardContentType = CardContentTypeResolver.resolve(value);
	}

}