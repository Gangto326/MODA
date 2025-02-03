package com.moda.moda_api.card.domain;

import java.util.Optional;

public interface UrlCacheRepository {
	UrlCache save(UrlCache urlCache);
	Optional<UrlCache> findByUrlHash(String urlHash);
}