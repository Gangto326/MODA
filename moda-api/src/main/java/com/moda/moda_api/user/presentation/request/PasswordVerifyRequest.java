package com.moda.moda_api.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordVerifyRequest {
	private String email;
	private String userId;
	private String code;

}
