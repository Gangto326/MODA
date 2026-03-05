package com.moda.moda_api.search.infrastructure.retry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.search.application.service.EsRetryService;
import com.moda.moda_api.search.domain.CardSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EsRetryScheduler {

	private static final int MAX_RETRY = 5;
	private static final int BATCH_SIZE = 20;

	private final EsRetryService esRetryService;
	private final CardSearchRepository cardSearchRepository;
	private final CardRepository cardRepository;

	@Scheduled(fixedDelay = 30000)
	public void processRetryQueue() {
		int processed = 0;

		while (processed < BATCH_SIZE) {
			EsRetryOperation operation = esRetryService.dequeue();
			if (operation == null) {
				break;
			}

			try {
				executeRetry(operation);
				processed++;
			} catch (Exception e) {
				operation.setRetryCount(operation.getRetryCount() + 1);

				if (operation.getRetryCount() >= MAX_RETRY) {
					log.error("ES retry exhausted ({}), discarding: operation={}, cardIds={}",
						MAX_RETRY, operation.getOperation(), operation.getCardIds());
				} else {
					log.warn("ES retry failed (attempt {}), requeuing: operation={}, cardIds={}",
						operation.getRetryCount(), operation.getOperation(), operation.getCardIds());
					esRetryService.requeue(operation);
				}
				processed++;
			}
		}
	}

	private void executeRetry(EsRetryOperation operation) {
		List<CardId> cardIds = operation.getCardIds().stream()
			.map(CardId::new)
			.collect(Collectors.toList());

		switch (operation.getOperation()) {
			case "SAVE" -> {
				Card card = cardRepository.findByCardId(cardIds.get(0))
					.orElseThrow(() -> new IllegalStateException(
						"Card not found for ES retry: " + cardIds.get(0).getValue()));
				cardSearchRepository.save(card);
			}
			case "SAVE_ALL" -> {
				List<Card> cards = cardIds.stream()
					.map(id -> cardRepository.findByCardId(id).orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
				if (!cards.isEmpty()) {
					cardSearchRepository.saveAll(cards);
				}
			}
			case "DELETE" -> cardSearchRepository.deleteAllById(cardIds);
			default -> log.error("Unknown ES retry operation: {}", operation.getOperation());
		}

		log.info("ES retry succeeded: operation={}, cardIds={}", operation.getOperation(), operation.getCardIds());
	}
}
