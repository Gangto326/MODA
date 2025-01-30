package com.moda.moda_api.summary.infrastructure;

import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.domain.model.Url;

public interface WebCrawler {
	CrawledContent crawl(Url url) throws Exception;

}
