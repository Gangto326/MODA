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
        println("현재 카테고리 목록: ${_categories.value}") // 카테고리 리스트 출력
        val name = _categories.value.find { it.categoryId == categoryId }?.category ?: " "
        _categoryName.value = name
        println("찾은 카테고리명: $name (categoryId: $categoryId)")
    }
}
