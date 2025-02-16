package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.User

data class UserProfileResponse(
    val email: String,
    val profileImage: String?,
    val nickname: String,
    val status: String
)

// 변환 함수 추가 ✅
fun UserProfileResponse.toDomainModel(): User {
    return User(
        email = this.email,
        profileImage = this.profileImage ?: "",
        nickname = this.nickname,
        status = this.status
    )
}
