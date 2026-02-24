package com.moda.moda_api.summary.domain;

import java.util.Optional;

public interface LilysRequestIdRepository {
	void save(String urlHash, String requestId);

	Optional<String> findByUrlHash(String urlHash);

	void delete(String urlHash);
}
