package com.example.modapjt.data.repository

import com.example.modapjt.data.api.SearchApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(private val api: SearchApiService) {

    suspend fun getAutoCompleteKeywords(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                api.getAutoCompleteKeywords(query) // ✅ 중복 제거
            } catch (e: Exception) {
                e.printStackTrace()
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
