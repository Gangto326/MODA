package com.example.modapjt.domain.model
// 홈 > 카테고리 선택 > 전체 탭

// 이미지 카드 (썸네일만 필요)
data class ImageCard(
    val imageUrl: String
)

// 동영상 카드 (제목, YouTube ID 필요)
data class VideoCard(
    val title: String,
    val youtubeId: String
)

// 블로그 카드 (제목, 썸네일 이미지, 요약 필요)
data class BlogCard(
    val title: String,
    val imageUrl: String,
    val description: String
)

// 뉴스 카드 (제목, 썸네일 이미지, 핵심 키워드 3개 필요)
data class NewsCard(
    val title: String,
    val imageUrl: String,
    val keywords: List<String>
)
