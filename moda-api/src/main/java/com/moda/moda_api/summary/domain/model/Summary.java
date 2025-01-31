package com.moda.moda_api.summary.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "post_summaries")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Summary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long summaryId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	private String summaryType;

	@Column(columnDefinition = "text")
	@JdbcTypeCode(SqlTypes.LONGVARCHAR)
	private String summaryData;

	private LocalDateTime createdAt;
}
