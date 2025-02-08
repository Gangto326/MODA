package com.moda.moda_api.image.domain;

import com.moda.moda_api.card.exception.InvalidCardIdException;

import lombok.Value;

@Value
public class ImageId {
	String value;

	public ImageId(String value){
		validateImageId(value);
		this.value =value;
	}

	private void validateImageId(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new InvalidCardIdException("카드 ID가 존재하지 않습니다.");
		}
	}

}
