package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Category

class CategoryRepository {
//    private val api = RetrofitInstance.api
    private val api = RetrofitInstance.categoryApi


    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategoryList()
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
