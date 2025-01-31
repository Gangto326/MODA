package com.moda.moda_api.summary.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sample_summary_posts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	@Id
	private String postId;
	private String title;
	private String url;
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "post")
	private List<Summary> summaries;
}