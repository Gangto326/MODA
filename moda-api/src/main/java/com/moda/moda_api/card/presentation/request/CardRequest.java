package com.moda.moda_api.card.presentation.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
	@URL(message = "유효하지 않은 URL 형식입니다. https:// 형식의 URL을 입력해주세요.",
		protocol = "https")
	private String url;

	@AssertTrue(message = "구글 URL은 사용할 수 없습니다.")
	public boolean isNotBlockedDomain() {
		if (url == null) return true;

		// 정규식으로 google 관련 도메인 차단
		return !url.matches("^(https?://)?(www\\.)?(google\\..*|.*\\.google\\..*)/.*$");
	}
}
