package com.example.modapjt.domain.model

import java.time.LocalDateTime

data class Board(
    val boardId: String,
    val userId: String,
    val title: String,
    val isPublic: Boolean, // true면 내용을 보여줌
    val position: Int,
    val createdAt: LocalDateTime,
    val cards: List<Card>, // 포함된 카드 목록
    val isView: Boolean, // 보드리스트에서 상세요약 보여질 보드

    // 비즈니스 로직
//    fun canAccess(currentUserId: String): Boolean {
//    return isPublic || userId == currentUserId
//}

//fun canEdit(currentUserId: String): Boolean {
//    return userId == currentUserId
//}
)