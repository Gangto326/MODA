package com.example.modapjt.data.dto.response

//  API에서 받은 데이터를 저장하는 DTO
import com.example.modapjt.domain.model.Board
import com.google.gson.annotations.SerializedName

// ✅ API 응답과 일치하도록 Board 객체를 감싸는 구조 추가
data class BoardDTO(
    @SerializedName("board") val boardData: BoardData  // board 객체를 감싸는 필드 추가
) {
    data class BoardData(
        @SerializedName("boardId") val boardId: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("title") val title: String,
        @SerializedName("position") val position: Int,
        @SerializedName("isPublic") val isPublic: Boolean,
        @SerializedName("createdAt") val createdAt: String
    )
}

// ✅ DTO → Domain 변환 함수 수정
fun BoardDTO.toDomain(): Board {
    return Board(
        boardId = this.boardData.boardId,
        title = this.boardData.title,
        createdAt = this.boardData.createdAt
    )
}
