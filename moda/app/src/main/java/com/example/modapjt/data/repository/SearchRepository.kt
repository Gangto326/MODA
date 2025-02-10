package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.SearchApiService
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
}
