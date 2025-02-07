package com.moda.moda_api.summary.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult.BlogPostResult;
import com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult.RawScriptResult;
import com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult.ShortSummaryResult;
import com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult.SummaryNoteResult;
import com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult.TimestampResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JsonMapper {
	private final ObjectMapper mapper;

	public JsonNode processSummaryNote(JsonNode jsonNode) throws JsonProcessingException {
		SummaryNoteResult summaryNote = mapper.treeToValue(jsonNode, SummaryNoteResult.class);
		return mapSummaryNoteListToJson(summaryNote.getData().getData().getSummaryNote());
	}

	// 우리만의 방식으로 다시 Json만들기
	public JsonNode mapSummaryNoteListToJson(List<SummaryNoteResult.SummaryNoteEntry> entries) throws JsonProcessingException {
		ArrayNode arrayNode = mapper.createArrayNode();
		for (SummaryNoteResult.SummaryNoteEntry entry : entries) {
			ObjectNode entryNode = mapper.createObjectNode();
			entryNode.put("timestamp", entry.getTimestamp());
			entryNode.put("title", entry.getTitle());
			entryNode.putPOJO("content", entry.getContent());
			arrayNode.add(entryNode);
		}
		return arrayNode;
	}
}