package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Category

class CategoryRepository {
    private val api = RetrofitInstance.categoryApi

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomain() ?: emptyList())
            } else {
                Result.failure(Exception("데이터 가져오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
//class CategoryRepository(private val tokenManager: TokenManager) {
//    private val api = RetrofitInstance.getCategoryApi(tokenManager)
//
//    suspend fun getCategories(): List<Category> {
//        val response = api.getCategories()
//        if (response.isSuccessful) {
//            return response.body()?.map { it.toDomain() } ?: emptyList()
//        } else {
//            throw Exception("Failed to fetch categories: ${response.code()}")
//        }
//    }
//}