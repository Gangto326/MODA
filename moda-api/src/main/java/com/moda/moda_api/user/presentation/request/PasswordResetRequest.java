package com.moda.moda_api.user.presentation.request;

import lombok.Getter;

@Getter
public class PasswordResetRequest {
	private String UserId;
	private String email;
	private String newPassword;
	private String newPasswordConfirm;
}
