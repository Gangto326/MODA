package com.moda.moda_api.card.presentation.request;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
	@URL(message = "유효하지 않은 URL 형식입니다. https:// 형식의 URL을 입력해주세요.",
		protocol = "https")
	private String url;

}