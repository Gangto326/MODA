package com.example.modapjt.domain.model

data class User(
    val userId: String,
    val email: String,
    val profileImage: String?, // nullabe 처리(String?) -> 일부 사용자가 이미지가 없을 수도 있음
    val nickname: String,
    val status: String
)
