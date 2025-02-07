package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Category

data class CategoryDTO(
    val categoryId: Int,
    val category: String,
    val position: Int
)

// DTO → Domain 변환 함수
fun CategoryDTO.toDomain(): Category {
    return Category(
        categoryId = this.categoryId,
        category = this.category,
        position = this.position
    )
}

// List<CategoryDTO> → List<Category> 변환 함수
fun List<CategoryDTO>.toDomain(): List<Category> {
    return this.map { it.toDomain() }
}
