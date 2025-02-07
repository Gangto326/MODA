package com.moda.moda_api.summary.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContentItem {
	private String content;
	private ContentItemType type;

	public static String concatenateTextContent(List<ContentItem> items) {
		if (items == null || items.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (ContentItem item : items) {
			if (item.getType() == ContentItemType.TEXT && item.getContent() != null) {
				builder.append(item.getContent());
			}
		}
		return builder.toString();
	}
}
