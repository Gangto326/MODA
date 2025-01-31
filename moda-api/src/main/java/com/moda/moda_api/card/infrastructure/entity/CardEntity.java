package com.moda.moda_api.card.infrastructure.entity;

import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import com.moda.moda_api.card.infrastructure.converter.VectorConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cards")
public class CardEntity {
    @Id
    @Column(name = "card_id", length = 36, nullable = false)
    private String cardId;

    @Column(name = "board_id", length = 36, nullable = false)
    private String boardId;

    @Column(name = "type_id", nullable = false)
    private Integer typeId;

    @Column(name = "url_hash", length = 64, nullable = false)
    private String urlHash;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "thumbnail_content", columnDefinition = "TEXT", nullable = false)
    private String thumbnailContent;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT", nullable = false)
    private String thumbnailUrl;

    @Column(name = "embedding", columnDefinition = "VECTOR(768)")
    @Convert(converter = VectorConverter.class)
    private float[] embedding;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private ContentTypeEntity contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_hash", insertable = false, updatable = false)
    private UrlCacheEntity urlCache;
}
