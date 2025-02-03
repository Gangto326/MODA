package com.example.modapjt.domain.model

// 앱에서 사용할 Board 데이터 모델 (DAO)
//data class Board(
//    val boardId: String?, // boardId를 nullable로 변경
//    val userId: String,
//    val title: String,
//    val isPublic: Boolean, // true면 내용을 보여줌
//    val position: Int,
//    val createdAt: String // 날짜 변환 (String → LocalDateTime)
//    val cards: List<Card>, // 포함된 카드 목록
//)

data class Board(
    val boardId: String,
    val title: String,
    val createdAt: String
)

