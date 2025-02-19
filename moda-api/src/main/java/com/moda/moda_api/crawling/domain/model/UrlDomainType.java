package com.moda.moda_api.crawling.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;


//
//
@Getter
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum UrlDomainType {
	UNCLASSIFIED(1),
	TISTORY(2),
	NAVER_BLOG(2),
	VELOG(2),
	NAVER_NEWS(3),
	DAUM_NEWS(3),
	YOUTUBE(1),
	GOOGLE_SEARCH(6),
	NAMUWIKI(2),
	BRUNCH(2),
	MOBILESPORTS(1),
	NAVER_SPORTS(3);

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