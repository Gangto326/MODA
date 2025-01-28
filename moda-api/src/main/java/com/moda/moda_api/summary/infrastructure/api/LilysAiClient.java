package com.moda.moda_api.summary.infrastructure.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.dto.SummaryResult;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LilysAiClient {
	private final RestTemplate restTemplate;

	@Value("${lilys.ai.api.url}")
	private String apiUrl;

	@Value("${lilys.ai.api.key}")
	private String apiKey;

	public LilysAiClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public LilysAiResponse callAi(String url) {
		try {
			HttpHeaders headers = createHeaders();
			Map<String, Object> requestMap = createRequestBody(url);

			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);
			ResponseEntity<LilysAiResponse> response = restTemplate.exchange(
				apiUrl,
				HttpMethod.POST,
				requestEntity,
				LilysAiResponse.class
			);

			if (response.getBody() == null) {
				throw new SummaryProcessingException("Received null response body from AI service");
			}

			return response.getBody();
		} catch (Exception e) {
			log.error("Error calling Lilys AI API", e);
			throw new SummaryProcessingException("Failed to call AI service", e);
		}
	}
	public <T> SummaryResult<T> getSummaryResult(String requestId, String resultType) {
		try {
			HttpHeaders headers = createHeaders();
			String url = UriComponentsBuilder.fromUriString(apiUrl)
				.pathSegment(requestId)
				.queryParam("resultType", resultType)
				.toUriString();

			HttpEntity<?> entity = new HttpEntity<>(headers);

			ParameterizedTypeReference<SummaryResult<T>> typeReference =
				(ParameterizedTypeReference<SummaryResult<T>>)getTypeReference(resultType);

			ResponseEntity<SummaryResult<T>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				typeReference
			);

			SummaryResult<T> result = response.getBody();
			if (result == null) {
				throw new SummaryProcessingException("Received null response body");
			}
			return result;
		} catch (Exception e) {
			log.error("Error getting summary result for requestId: {}, resultType: {}", requestId, resultType, e);
			throw new SummaryProcessingException("Failed to get summary result", e);
		}
	}

	private ParameterizedTypeReference<?> getTypeReference(String resultType) {
		switch (resultType) {
			case "blogPost":
				return new ParameterizedTypeReference<SummaryResult<SummaryResult.BlogPostType>>() {};
			case "summaryNote":
				return new ParameterizedTypeReference<SummaryResult<SummaryResult.SummaryNoteType>>() {};
			case "rawScript":
				return new ParameterizedTypeReference<SummaryResult<SummaryResult.RawScriptType>>() {};
			case "shortSummary":
				return new ParameterizedTypeReference<SummaryResult<SummaryResult.ShortSummaryType>>() {};
			case "timestamp":
				return new ParameterizedTypeReference<SummaryResult<SummaryResult.TimestampType>>() {};
			default:
				throw new IllegalArgumentException("Unsupported resultType: " + resultType);
		}
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + apiKey);
		return headers;
	}

	private Map<String, Object> createRequestBody(String url) {
		Map<String, Object> source = new HashMap<>();

		if(url.contains("youtube.com")) {
			source.put("sourceType", "youtube_video");
		}else{
			source.put("sourceType", "webPage");
		}

		source.put("sourceUrl", url);

		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("source", source);
		requestMap.put("resultLanguage", "ko");
		requestMap.put("modelType", "gpt-3.5");

		return requestMap;
	}

}
