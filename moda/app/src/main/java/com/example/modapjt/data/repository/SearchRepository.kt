package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.api.SearchApiService
import com.example.modapjt.data.dto.response.HomeKeywordResponse
import com.example.modapjt.data.dto.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SearchRepository(private val api: SearchApiService) {

    suspend fun getAutoCompleteKeywords(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("API_REQUEST", "서버에 요청 중: query=$query") // ✅ API 요청 로그 추가

                val response = api.getAutoCompleteKeywords(query)

                Log.d("API_SUCCESS", "서버 응답 성공: $response") // ✅ API 응답 성공 로그 추가

                response
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_ERROR", "API 요청 실패: ${e.message}") // ✅ API 오류 로그 추가
                emptyList()
            }
        }
    }


    suspend fun getInterestKeywords(keyword: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                api.getInterestKeywords(keyword) // ✅ 중복 제거
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    /**
     * 검색 API를 호출하여 데이터를 가져옴
     * @param userId 사용자 ID
     * @param onSuccess 성공 시 데이터를 반환하는 콜백 함수
     * @param onFailure 실패 시 호출될 콜백 함수
     */
    suspend fun getSearchData(userId: String): SearchResponse? {
        return try {
            RetrofitInstance.searchApi.searchMain(userId) // ✅ suspend 함수로 직접 호출
        } catch (e: Exception) {
            e.printStackTrace()
            null // 실패 시 null 반환
        }
    }

    suspend fun getHomeKeyword(userId: String): HomeKeywordResponse {
        return try {
            api.homeKeyword(userId) // 전체 HomeKeywordResponse 반환
        } catch (e: Exception) {
            Log.e("HomeKeywordRepository", "Error fetching home keyword", e)
            HomeKeywordResponse(emptyList(), "") // 에러 발생 시 빈 데이터 반환
        }
    }
}
