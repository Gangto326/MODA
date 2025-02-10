package com.moda.moda_api.crawling.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum UrlDomainType {
	UNCLASSIFIED(1),
	TISTORY(2),
	NAVER_BLOG(3),
	VELOG(4),
	NAVER_NEWS(5),
	DAUM_NEWS(6),
	YOUTUBE(7),
	GOOGLE_SEARCH(8);

	private final Integer typeId;

	UrlDomainType(Integer typeId){
		this.typeId = typeId;
	}

	@JsonValue
	public Integer getTypeId() {
		return typeId;
	}

	@JsonCreator
	public static UrlDomainType fromTypeId(Integer typeId) {
		for (UrlDomainType type : values()) {
			if (type.getTypeId().equals(typeId)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown typeId: " + typeId);
	}
}