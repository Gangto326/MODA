package com.example.modapjt.data.dto.response

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
    val meta: Meta?
)

data class Meta(
    val total_count: Int,
    val page: Int,
    val size: Int,
    val has_next: Boolean
)