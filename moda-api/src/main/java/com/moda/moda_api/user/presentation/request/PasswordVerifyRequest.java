package com.moda.moda_api.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVerifyRequest {
	private String email;
	private String userId;
	private String code;

}
