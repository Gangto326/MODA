package com.moda.moda_api.summary.infrastructure;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.moda.moda_api.summary.domain.model.Content;
import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.domain.model.Url;

@Component
public class JsoupWebCrawler implements WebCrawler {
	@Override
	public CrawledContent crawl(Url url) throws Exception {
		try {
			Document doc = Jsoup.connect(url.getValue()).get();
			String title = doc.title();
			String body = doc.body().text();
			return new CrawledContent(url.getValue(), title, body);
		} catch (IOException e) {
			throw new Exception("Failed to crawl URL: " + url.getValue(), e);
		}
	}
}