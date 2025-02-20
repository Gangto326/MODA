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

    fun loadCategories() {
        viewModelScope.launch {
            val result = repository.getCategories()
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
            2 -> "건강"
            3 -> "여행"
            4 -> "음식"
            5 -> "IT"
            6 -> "경제"
            7 -> "문화"
            8 -> "과학"
            9 -> "취미"
            10 -> "예술"
            else -> "기타"
        }
        println("카테고리 ID: $categoryId, 찾은 카테고리명: ${_categoryName.value}")
    }
}
