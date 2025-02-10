package com.moda.moda_api.summary.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.summary.infrastructure.api.YoutubeApiClient;
import com.moda.moda_api.summary.infrastructure.dto.YoutubeAPIResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeApiTestController {

	private final YoutubeApiClient youtubeApiClient;

	@GetMapping("/video/{videoId}")
	public YoutubeAPIResponseDTO testVideoApi(@PathVariable String videoId) {
		return youtubeApiClient.getVideoData(videoId);
	}
}