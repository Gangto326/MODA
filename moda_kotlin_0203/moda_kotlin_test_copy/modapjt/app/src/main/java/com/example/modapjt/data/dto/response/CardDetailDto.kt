

//package com.example.modapjt.data.dto.response
//
//import com.example.modapjt.domain.model.Card
//import com.example.modapjt.domain.model.CardDetail
//import com.google.gson.annotations.SerializedName
//
//// 카드 상세 조회용 DTO (새로운 /api/card/{cardId} 용)
//data class CardDetailDTO(
//    @SerializedName("cardId") val cardId: String,
//    @SerializedName("boardId") val boardId: String,
//    @SerializedName("typeId") val typeId: Int,
//    @SerializedName("type") val type: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("content") val content: String,
//    @SerializedName("createdAt") val createdAt: String
//)
//
//// 상세 조회용 DTO → Domain 변환 함수
//fun CardDetailDTO.toDomain(): CardDetail {
//    return CardDetail(
//        cardId = this.cardId,
//        boardId = this.boardId,
//        typeId = this.typeId,
//        type = this.type,
//        title = this.title,
//        content = this.content,
//        createdAt = this.createdAt
//    )
//}


//package com.example.modapjt.data.dto.response
//
//import com.example.modapjt.domain.model.CardDetail
//import com.google.gson.annotations.SerializedName
//import org.json.JSONObject
//
//data class CardDetailDTO(
//    @SerializedName("cardId") val cardId: String,
//    @SerializedName("boardId") val boardId: String,
//    @SerializedName("typeId") val typeId: Int,
//    @SerializedName("type") val type: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("content") val content: String,
//    @SerializedName("createdAt") val createdAt: String
//) {
//    // content JSON 파싱
//    fun parseContent(): List<ContentSection> {
//        return try {
//            val json = JSONObject(content)
//            val resultArray = json.getJSONArray("result")
//            List(resultArray.length()) { i ->
//                val item = resultArray.getJSONObject(i)
//                ContentSection(
//                    key = item.getString("key"),
//                    value = item.getDouble("value")
//                )
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//}
//
//// content 파싱 결과를 담는 데이터 클래스
//data class ContentSection(
//    val key: String,
//    val value: Double
//)
//
//
//// 상세 조회용 DTO → Domain 변환 함수
//fun CardDetailDTO.toDomain(): CardDetail {
//    val sections = parseContent()
//    return CardDetail(
//        cardId = this.cardId,
//        boardId = this.boardId,
//        typeId = this.typeId,
//        type = this.type,
//        title = this.title,
//        content = sections.toString(),
//        createdAt = this.createdAt
//    )
//}







//package com.example.modapjt.data.dto.response
//
//import com.example.modapjt.domain.model.CardDetail
//import com.google.gson.annotations.SerializedName
//import org.json.JSONObject
//
//data class CardDetailDTO(
//    @SerializedName("cardId") val cardId: String,
//    @SerializedName("boardId") val boardId: String,
//    @SerializedName("typeId") val typeId: Int,
//    @SerializedName("type") val type: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("content") val content: String,
//    @SerializedName("createdAt") val createdAt: String
//) {
//    // content JSON 파싱
//    fun parseContent(): List<ContentSection> {
//        return try {
//            val json = JSONObject(content)
//            val resultArray = json.getJSONArray("result")
//            List(resultArray.length()) { i ->
//                val item = resultArray.getJSONObject(i)
//                ContentSection(
//                    key = item.getString("key"),
//                    value = item.getDouble("value")
//                )
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//
//    fun toDomain(): CardDetail {
//        val sections = parseContent()
//        return CardDetail(
//            cardId = this.cardId,
//            boardId = this.boardId,
//            typeId = this.typeId,
//            type = this.type,
//            title = this.title,
//            content = sections,
//            createdAt = this.createdAt
//        )
//    }
//}
//
//// content 파싱 결과를 담는 데이터 클래스
//data class ContentSection(
//    val key: String,
//    val value: Double
//)





package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.CardDetail
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class CardDetailDTO(
    @SerializedName("cardId") val cardId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("typeId") val typeId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String
) {
    // content JSON 파싱
//    fun parseContent(): List<BlogPost> {
//        return try {
//            val json = JSONObject(content)
//            val resultArray = json.getJSONArray("result")
//            List(resultArray.length()) { i ->
//                val item = resultArray.getJSONObject(i)
//                BlogPost(
//                    title = item.getString("title"),
//                    content = item.getString("content")
//                )
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
    fun parseContent(): List<BlogPost> {
        return try {
            val json = JSONObject(content)  // ✅ JSON 파싱
            val blogPostsArray = json.optJSONArray("blogPost") ?: JSONArray()  // ✅ "blogPost" 키 사용

            List(blogPostsArray.length()) { i ->
                val item = blogPostsArray.getJSONObject(i)
                BlogPost(
                    title = item.optString("title", "제목 없음"),  // ✅ `optString()` 사용하여 안전하게 파싱
                    content = item.optString("content", "내용 없음")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    fun toDomain(): CardDetail {
        val sections = parseContent()
        return CardDetail(
            cardId = this.cardId,
            boardId = this.boardId,
            typeId = this.typeId,
            type = this.type,
            title = this.title,
            content = sections,
            createdAt = this.createdAt
        )
    }
}

data class BlogPost(
    val title: String,
    val content: String
)
