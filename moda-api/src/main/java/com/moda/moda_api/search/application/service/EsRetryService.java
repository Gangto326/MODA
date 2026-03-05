package com.moda.moda_api.search.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.search.application.event.CardSearchEvent;
import com.moda.moda_api.search.infrastructure.retry.EsRetryOperation;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EsRetryService {

	private static final String RETRY_QUEUE_KEY = "es:retry:queue";

	private final RedisTemplate<String, String> esRetryRedisTemplate;
	private final ObjectMapper objectMapper;

	public EsRetryService(
		@Qualifier("esRetryRedisTemplate") RedisTemplate<String, String> esRetryRedisTemplate,
		ObjectMapper objectMapper
	) {
		this.esRetryRedisTemplate = esRetryRedisTemplate;
		this.objectMapper = objectMapper;
	}

	public void enqueue(CardSearchEvent event) {
		List<String> cardIds;
		switch (event.getOperation()) {
			case SAVE -> cardIds = List.of(event.getCard().getCardId().getValue());
			case SAVE_ALL -> cardIds = event.getCards().stream()
				.map(card -> card.getCardId().getValue())
				.collect(Collectors.toList());
			case DELETE -> cardIds = event.getCardIds().stream()
				.map(cardId -> cardId.getValue())
				.collect(Collectors.toList());
			default -> throw new IllegalArgumentException("Unknown operation: " + event.getOperation());
		}

		EsRetryOperation operation = EsRetryOperation.builder()
			.operation(event.getOperation().name())
			.cardIds(cardIds)
			.retryCount(0)
			.createdAt(System.currentTimeMillis())
			.build();

		try {
			String json = objectMapper.writeValueAsString(operation);
			esRetryRedisTemplate.opsForList().leftPush(RETRY_QUEUE_KEY, json);
			log.info("ES retry enqueued: operation={}, cardIds={}", operation.getOperation(), cardIds);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize ES retry operation", e);
		}
	}

	public void requeue(EsRetryOperation operation) {
		try {
			String json = objectMapper.writeValueAsString(operation);
			esRetryRedisTemplate.opsForList().leftPush(RETRY_QUEUE_KEY, json);
		} catch (JsonProcessingException e) {
			log.error("Failed to requeue ES retry operation", e);
		}
	}

	public EsRetryOperation dequeue() {
		String json = esRetryRedisTemplate.opsForList().rightPop(RETRY_QUEUE_KEY);
		if (json == null) {
			return null;
		}
		try {
			return objectMapper.readValue(json, EsRetryOperation.class);
		} catch (JsonProcessingException e) {
			log.error("Failed to deserialize ES retry operation: {}", json, e);
			return null;
		}
	}

	public long getQueueSize() {
		Long size = esRetryRedisTemplate.opsForList().size(RETRY_QUEUE_KEY);
		return size != null ? size : 0;
	}
}
