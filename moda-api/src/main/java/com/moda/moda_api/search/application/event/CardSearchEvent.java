package com.moda.moda_api.search.application.event;

import java.util.List;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;

import lombok.Getter;

@Getter
public class CardSearchEvent {

	public enum Operation { SAVE, SAVE_ALL, DELETE }

	private final Operation operation;
	private final Card card;
	private final List<Card> cards;
	private final List<CardId> cardIds;

	private CardSearchEvent(Operation operation, Card card, List<Card> cards, List<CardId> cardIds) {
		this.operation = operation;
		this.card = card;
		this.cards = cards;
		this.cardIds = cardIds;
	}

	public static CardSearchEvent save(Card card) {
		return new CardSearchEvent(Operation.SAVE, card, null, null);
	}

	public static CardSearchEvent saveAll(List<Card> cards) {
		return new CardSearchEvent(Operation.SAVE_ALL, null, cards, null);
	}

	public static CardSearchEvent delete(List<CardId> cardIds) {
		return new CardSearchEvent(Operation.DELETE, null, null, cardIds);
	}
}
