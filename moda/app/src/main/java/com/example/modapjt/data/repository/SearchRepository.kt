package com.example.modapjt.data.repository

import com.example.modapjt.data.api.SearchApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(private val apiService: SearchApiService) {

    suspend fun getAutoCompleteKeywords(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAutoCompleteKeywords(query)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
