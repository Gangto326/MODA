//package com.example.modapjt.data.dto.response
//
//import com.example.modapjt.domain.model.Card
//import com.google.gson.annotations.SerializedName
//
//// 개별 카드 정보를 담는 DTO
//data class CardDTO(
//    @SerializedName("cardId") val cardId: String,
//    @SerializedName("boardId") val boardId: String,
//    @SerializedName("typeId") val typeId: Int,
//    @SerializedName("type") val type: String,
//    @SerializedName("thumbnailContent") val thumbnailContent: String?,
//    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
//    @SerializedName("createdAt") val createdAt: String
//)
//
//// DTO → Domain 변환 함수
//fun CardDTO.toDomain(): Card {
//    return Card(
//        cardId = this.cardId,
//        boardId = this.boardId,
//        typeId = this.typeId,
//        type = this.type,
//        thumbnailContent = this.thumbnailContent ?: "", // 기본값 제공
//        thumbnailUrl = this.thumbnailUrl ?: "", // 기본값 제공
//        createdAt = this.createdAt
//    )
//}
//
//// 서버 응답에서 thumbnailContent나 thumbnailUrl이 항상 존재하는 값이라면?
//// 방법 1) nullable 타입 유지 → Card.kt에서 그대로 String? 사용
//// null일 경우 기본적으로 빈 문자열("")을 사용해야 한다면?
//// 방법 2) 기본값 제공 → toDomain()에서 ?: "" 추가





package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.model.CardDetail
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

// 카드 목록 조회용 DTO (기존 /api/card 용)
data class CardDTO(
    @SerializedName("cardId") val cardId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("typeId") val typeId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
//    @SerializedName("thumbnailContent") val thumbnailContent: String?,
//    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("thumbnailContent") val thumbnailContent: String = "{}", // 기본값 추가
    @SerializedName("thumbnailUrl") val thumbnailUrl: String = "", // 기본값 추가
    @SerializedName("createdAt") val createdAt: String
) {
    // thumbnailContent JSON 파싱
//    fun parseThumbnailContent(): List<ThumbnailSection> {
//        return try {
//            val json = JSONObject(thumbnailContent ?: "{}")
//            val resultArray = json.getJSONArray("result")
//            List(resultArray.length()) { i ->
//                val item = resultArray.getJSONObject(i)
//                ThumbnailSection(
//                    key = item.getString("key"), // title -> key
//                    value = item.getDouble("value") // timestamp -> value
//                )
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
    fun parseThumbnailContent(): List<ThumbnailSection> {
        return try {
            if (thumbnailContent.isEmpty() || thumbnailContent == "{}") return emptyList()
            val json = JSONObject(thumbnailContent)

            // 1. "result" 배열이 있는 경우 (YouTube 같은 타입)
            if (json.has("result")) {
                val resultArray = json.getJSONArray("result")
                return List(resultArray.length()) { i ->
                    val item = resultArray.getJSONObject(i)
                    ThumbnailSection(
                        key = item.optString("title", ""), // title -> key
                        value = item.optString("timestamp", "") // timestamp -> value
                    )
                }
            }

            // 2. "summary" 필드가 있는 경우 (Blog 같은 타입)
            if (json.has("summary")) {
                return listOf(ThumbnailSection(
                    key = "", // key는 고정된 값 (혹은 "Blog Summary" 같은 값으로 변경 가능)
                    value = json.optString("summary", "No summary available") // summary 내용을 value에 저장
                ))
            }


            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}

// thumbnailContent 파싱 결과를 담는 데이터 클래스
data class ThumbnailSection(
    val key: String,
    val value: String
)

// 목록 조회용 DTO → Domain 변환 함수
fun CardDTO.toDomain(): Card {
    val sections = parseThumbnailContent()
    return Card(
        cardId = this.cardId,
        boardId = this.boardId,
        typeId = this.typeId,
        type = this.type,
        title = this.title,
        thumbnailContent = sections,
        thumbnailUrl = this.thumbnailUrl ?: "",
        createdAt = this.createdAt
    )
}



//// CardDTO.kt
//data class CardDTO(
//    @SerializedName("cardId") val cardId: String,
//    @SerializedName("boardId") val boardId: String,
//    @SerializedName("typeId") val typeId: Int,
//    @SerializedName("type") val type: String,
//    @SerializedName("thumbnailContent") val thumbnailContent: String?,
//    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
//    @SerializedName("createdAt") val createdAt: String
//) {
//    // 파싱 함수는 DTO 클래스의 멤버 함수로
//    fun parseThumbnailContent(): List<ThumbnailSection> {
//        return try {
//            // ... 기존 파싱 로직 ...
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//
//    // 변환 함수도 같은 파일에 확장 함수로
//    fun toDomain(): Card {
//        val sections = parseThumbnailContent()
//        return Card(
//            cardId = this.cardId,
//            boardId = this.boardId,
//            typeId = this.typeId,
//            type = this.type,
//            thumbnailContent = sections,
//            thumbnailUrl = this.thumbnailUrl ?: "",
//            createdAt = this.createdAt
//        )
//    }
//}