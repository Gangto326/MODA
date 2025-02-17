package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.User

data class UserStatusResponse(
    val nickname: String,
    val allCount: String,
    val bookmarkCount: String
)

//// 변환 함수 추가
//fun UserStatusResponse.toDomain(): User {
//    return User(
//        nickname = this.nickname,
//        allCount = this.allCount,
//        bookmarkCount = this.bookmarkCount
//    )
//}


