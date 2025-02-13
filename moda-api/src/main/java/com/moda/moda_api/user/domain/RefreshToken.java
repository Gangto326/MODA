package com.moda.moda_api.user.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshToken {
	private Long refreshTokenId;
	private String token;
	private LocalDateTime expiresAt;
	private LocalDateTime createdAt;
	private boolean isActive;

	public void deactivate() {
		this.isActive = false;
	}
}
