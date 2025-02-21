package com.moda.moda_api.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {
	@NotBlank(message = "이메일은 필수 입력값입니다.")
		private String email;
}