package com.moda.moda_api.summary.infrastructure.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.summary.infrastructure.dto.TitleAndContent;
import com.moda.moda_api.summary.infrastructure.dto.lilysummarytyperesult.SummaryNoteResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JsonMapper {
	private final ObjectMapper mapper;

	public List<TitleAndContent> processSummaryNote(JsonNode jsonNode) throws JsonProcessingException {
		SummaryNoteResult summaryNote = mapper.treeToValue(jsonNode, SummaryNoteResult.class);
		return convertToTitleAndContent(summaryNote.getData().getData().getSummaryNote());
	}

	private List<TitleAndContent> convertToTitleAndContent(List<SummaryNoteResult.SummaryNoteEntry> entries) {
		return entries.stream()
			.map(entry -> new TitleAndContent(
				entry.getTitle(),
				String.join("\n", entry.getContent())
			))
			.collect(Collectors.toList());
	}

	// timestamp 배열이 필요한 경우를 위한 메서드
	public String[] extractTimestamps(JsonNode jsonNode) throws JsonProcessingException {
		SummaryNoteResult summaryNote = mapper.treeToValue(jsonNode, SummaryNoteResult.class);
		return summaryNote.getData().getData().getSummaryNote().stream()
			.map(entry -> String.valueOf(entry.getTimestamp()))
			.toArray(String[]::new);
	}
}