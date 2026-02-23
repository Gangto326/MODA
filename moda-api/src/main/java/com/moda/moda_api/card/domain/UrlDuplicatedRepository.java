package com.moda.moda_api.card.domain;

public interface UrlDuplicatedRepository {

	void urlDuplicatedSave(String url);

	Boolean checkDuplicated(String url);

	void urlDuplicatedDelete(String url);
}
