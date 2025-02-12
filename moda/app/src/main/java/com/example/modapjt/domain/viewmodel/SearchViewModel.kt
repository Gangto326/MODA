package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.SearchResponse
import com.example.modapjt.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository(RetrofitInstance.searchApi)

    // ✅ AutoComplete 검색 결과 (StateFlow 유지)
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults.asStateFlow()

    // ✅ 홈화면의 많은 데이터들 (StateFlow로 변경하여 자동 UI 업데이트 가능)
    private val _searchData = MutableStateFlow<SearchResponse?>(null)
    val searchData: StateFlow<SearchResponse?> = _searchData.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ✅ 자동완성 키워드 API 호출
    fun fetchAutoCompleteKeywords(query: String) {
        viewModelScope.launch {
            val results = repository.getAutoCompleteKeywords(query)
            _searchResults.value = results
        }
    }

    // ✅ 새로고침 또는 첫 화면 진입 시 API 호출
    fun loadSearchData(userId: String) {
        viewModelScope.launch {
            try {
                println("[SearchViewModel] API 호출: 사용자 ID = $userId")
                val response = repository.getSearchData(userId)
                _searchData.value = response
                println("[SearchViewModel] 데이터 로드 완료")
            } catch (e: Exception) {
                _error.value = e.message
                println("[SearchViewModel] 오류 발생: ${e.message}")
            }
        }
    }
}
