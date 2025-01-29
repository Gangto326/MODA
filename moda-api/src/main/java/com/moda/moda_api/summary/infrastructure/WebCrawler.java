package com.moda.moda_api.summary.infrastructure;

import com.moda.moda_api.summary.domain.model.CrawledContent;

public interface WebCrawler {
	CrawledContent crawl(Url url) throws Exception;

}
