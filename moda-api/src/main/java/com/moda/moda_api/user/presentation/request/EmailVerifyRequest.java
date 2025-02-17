package com.moda.moda_api.user.presentation.request;

import lombok.Getter;

@Getter
public class EmailVerifyRequest {
	private String email;
	private String code;
}
