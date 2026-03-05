package com.moda.moda_api.search.application.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.moda.moda_api.search.application.service.EsRetryService;
import com.moda.moda_api.search.domain.CardSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardSearchEventListener {

	private final CardSearchRepository cardSearchRepository;
	private final EsRetryService esRetryService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleCardSearchEvent(CardSearchEvent event) {
		try {
			switch (event.getOperation()) {
				case SAVE -> cardSearchRepository.save(event.getCard());
				case SAVE_ALL -> cardSearchRepository.saveAll(event.getCards());
				case DELETE -> cardSearchRepository.deleteAllById(event.getCardIds());
			}
			log.debug("ES indexing succeeded: operation={}", event.getOperation());
		} catch (Exception e) {
			log.error("ES indexing failed, enqueuing retry: {}", event.getOperation(), e);
			esRetryService.enqueue(event);
		}
	}
}
