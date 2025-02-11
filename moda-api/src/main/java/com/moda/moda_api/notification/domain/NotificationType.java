package com.moda.moda_api.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
	user("사용자 알림"),
	card("카드 알림");

	private final String description;

	NotificationType(String description) {
		this.description = description;
	}
}