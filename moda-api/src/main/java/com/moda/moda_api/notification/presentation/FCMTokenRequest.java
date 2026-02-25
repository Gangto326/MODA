package com.moda.moda_api.notification.presentation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCMTokenRequest {
	private String token;
}
