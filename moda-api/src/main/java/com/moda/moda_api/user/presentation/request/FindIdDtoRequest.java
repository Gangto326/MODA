package com.moda.moda_api.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindIdDtoRequest {
	private String email;
	private String code;
}
