package com.moda.moda_api.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
	USER("사용자 알림"),
	BOARD("게시글 알림"),
	CARD("카드 알림");

	private final String description;

	NotificationType(String description) {
		this.description = description;
	}
}