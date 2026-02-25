package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.BookmarkRequest
import com.example.modapjt.data.dto.request.CardRequest
import com.example.modapjt.data.dto.response.AllTabCardApiResponse
import com.example.modapjt.data.dto.response.CardDetailDTO
import com.example.modapjt.data.dto.response.TabCardApiResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

// API 요청을 정의하는 인터페이스
interface CardApiService {
    // 카드 리스트 API 구버전
//    @GET("api/card")
//    suspend fun getCardList(
//        @Query("userId") userId: String,
//        @Query("categoryId") categoryId: Int,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("sortBy") sortBy: String = "createdAt",
//        @Query("sortDirection") sortDirection: String = "DESC"
//    ): Response<CardApiResponse>

    // 전체탭 카드 리스트 API 재설정
    @GET("api/search/tab") // main -> tab으로 변경
    suspend fun getAllTabCardList(
        @Query("query") query: String,
        @Query("categoryId") categoryId: Int
    ): Response<AllTabCardApiResponse>

    // 그 외 탭(이미지탭, 블로그탭, .. ) 카드 리스트 API 재설정
    @GET("api/search")
    suspend fun getTabCardList(
        @Query("query") query: String,
        @Query("categoryId") categoryId: Int,
        @Query("typeId") typeId: Int,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDirection") sortDirection: String // 동적으로 반영
    ): Response<TabCardApiResponse>

    // 카드 상세 페이지 API
    @GET("api/card/{cardId}")
    suspend fun getCardDetail(
        @Path("cardId") cardId: String,
    ): Response<CardDetailDTO>

    // 카드 저장 API
    @POST("api/card")
    suspend fun createCard(
        @Body cardRequest: CardRequest
    ): Response<Boolean>

    // 카드 삭제 API
    @DELETE("api/card/{cardIds}")
    suspend fun deleteCard(
        @Path("cardIds") cardIds: String // "id1,id2,id3" 형태로 전달
    ): Response<Boolean>


    // 즐겨찾기 전체탭 카드 리스트 API
    @GET("api/search/bookmark")
    suspend fun getAllTabBookMarkCardList(
    ): Response<AllTabCardApiResponse>

    // 즐겨찾기 그 외 탭(이미지탭, 블로그탭, .. ) 카드 리스트 API 재설정
    @GET("api/search/bookmark/type")
    suspend fun getTabBookMarkCardList(
        @Query("typeId") typeId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int = 15, // 15개씩 불러오기
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDirection") sortDirection: String // 동적으로 반영
    ): Response<TabCardApiResponse>

    @POST("api/card/bookmark")
    suspend fun toggleBookmark(
        @Body bookmarkRequest: BookmarkRequest
    ): Response<Boolean>  // ApiResponse<Unit> 대신 Boolean으로 변경

    // 이미지 저장을 위한 새로운 API
    @Multipart
    @POST("api/card")
    suspend fun createCardWithImage(
        @Part files: List<MultipartBody.Part>
    ): Response<Boolean>
}