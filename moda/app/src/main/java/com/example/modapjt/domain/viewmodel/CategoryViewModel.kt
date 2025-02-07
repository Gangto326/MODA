package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CategoryRepository
import com.example.modapjt.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val repository = CategoryRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _categoryName = MutableStateFlow("전체") // 기본값 설정
    val categoryName: StateFlow<String> = _categoryName

    fun loadCategories(userId: String) {
        viewModelScope.launch {
            val result = repository.getCategories(userId)
            if (result.isSuccess) {
                _categories.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // categoryId에 해당하는 categoryName 업데이트
    fun updateCategoryName(categoryId: Int) {
        val name = _categories.value.find { it.categoryId == categoryId }?.category ?: " "
        _categoryName.value = when (categoryId) {
            1 -> "전체"
            2 -> "트렌드"
            3 -> "오락"
            4 -> "금융"
            5 -> "여행"
            6 -> "음식"
            7 -> "IT"
            8 -> "디자인"
            9 -> "사회"
            10 -> "건강"
            else -> "전체"
        }
        println("카테고리 ID: $categoryId, 찾은 카테고리명: ${_categoryName.value}")
    }
}
