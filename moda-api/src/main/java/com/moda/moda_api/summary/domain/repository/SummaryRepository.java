package com.moda.moda_api.summary.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moda.moda_api.summary.domain.model.Summary;

public interface SummaryRepository extends JpaRepository<Summary,Long> {
	List<Summary> findByPost_PostId(String postId);
	List<Summary> findByPost_PostIdAndSummaryType(String postId, String summaryType);
}
