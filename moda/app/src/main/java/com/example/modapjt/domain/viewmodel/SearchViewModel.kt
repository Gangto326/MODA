package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository(RetrofitInstance.apiService)

    private val _searchResults = MutableStateFlow<List<String>>(emptyList()) // ✅ 다시 StateFlow 사용
    val searchResults: StateFlow<List<String>> get() = _searchResults

    fun fetchAutoCompleteKeywords(query: String) {
        viewModelScope.launch {
            val results = repository.getAutoCompleteKeywords(query)
            _searchResults.value = results // ✅ 기존 방식으로 유지
        }
    }
}
