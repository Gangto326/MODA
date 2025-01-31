package com.moda.moda_api.summary.infrastructure.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.summary.domain.model.Post;
import com.moda.moda_api.summary.domain.model.Summary;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.BlogPostResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.RawScriptResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.ShortSummaryResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.SummaryNoteResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.TimestampResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SummaryMapper {
	private final ObjectMapper objectMapper;

	public List<Summary> createSummaries(String requestId, String url, Object[] results) {
		Post post = createPost(requestId, url);
		List<Summary> summaries = new ArrayList<>();

		for (Object result : results) {
			try {
				Summary summary = buildSummary(result, post);
				summaries.add(summary);
			} catch (JsonProcessingException e) {
				log.error("Failed to convert summary data to JSON", e);
			}
		}
		return summaries;
	}

	private Summary buildSummary(Object result, Post post) throws JsonProcessingException {
		Summary.SummaryBuilder builder = Summary.builder()
			.post(post)
			.createdAt(LocalDateTime.now());
		JsonNode rootNode = objectMapper.valueToTree(result);
		JsonNode extractedData = rootNode.path("data").path("data");
		JsonNode summaryTypeData =rootNode.path("data").path("type");
		builder.summaryType(objectMapper.writeValueAsString(summaryTypeData));
		builder.summaryData(objectMapper.writeValueAsString(extractedData));
		return builder.build();
	}



	private Post createPost(String requestId, String url) {
		return Post.builder()
			.postId(requestId)
			.url(url)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
