package com.example.modapjt.data.repository

import com.example.modapjt.data.api.UserApiService
import com.example.modapjt.data.dto.response.UserProfileResponse
import com.example.modapjt.data.dto.response.toDomainModel
import com.example.modapjt.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val api: UserApiService) {
    suspend fun getUser(userId: String): User {
        return withContext(Dispatchers.IO) {
            val response: UserProfileResponse = api.getUserProfile(userId)
            response.toDomainModel()
        }
    }
}
