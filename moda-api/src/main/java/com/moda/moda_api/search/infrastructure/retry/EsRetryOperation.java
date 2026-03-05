package com.moda.moda_api.search.infrastructure.retry;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsRetryOperation {
	private String operation;      // "SAVE" | "SAVE_ALL" | "DELETE"
	private List<String> cardIds;
	private int retryCount;
	private long createdAt;
}
