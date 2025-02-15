package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.HotTopicItem
import com.example.modapjt.data.dto.response.KeywordSearchResponse
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

    // ✅ 영상보고갈래요?? 에 키워드 들어가는것
    private val _creator = MutableStateFlow("")
    val creator: StateFlow<String> get() = _creator

    // ✅ 이번주 키워드에 들어갈 5개의 키워드 리스트
    private val _topKeywords = MutableStateFlow<List<String>>(emptyList())
    val topKeywords: StateFlow<List<String>> get() = _topKeywords

    // ✅ 위의 두개를 한꺼번에 가지고 오는 것.
    private val _selectedKeyword = MutableStateFlow<String?>(null)
    val selectedKeyword: StateFlow<String?> = _selectedKeyword.asStateFlow()


    // ✅ 키워드 검색 결과만 따로 저장
    private val _keywordSearchData = MutableStateFlow<List<KeywordSearchResponse>>(emptyList())
    val keywordSearchData: StateFlow<List<KeywordSearchResponse>> = _keywordSearchData.asStateFlow()

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


    // ✅ 이번주 키워드 5개 가지고 오는 API 호출
    fun fetchHomeKeywords(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getHomeKeyword(userId)
                _topKeywords.value = response.topKeywords // ✅ 키워드 리스트 업데이트
                _creator.value = response.creator // ✅ creator 업데이트
            } catch (e: Exception) {
                _topKeywords.value = emptyList()
                _creator.value = ""
            }
        }
    }



    // ✅ 키워드 검색 시 API 호출 (KeywordSearchResponse 저장)
    fun updateKeywordAndFetchData(keyword: String, userId: String) {
        if (_selectedKeyword.value == keyword) return // 이미 선택된 키워드면 무시
        _selectedKeyword.value = keyword

        viewModelScope.launch {
            try {
                println("[SearchViewModel] 키워드 검색 API 호출: $keyword")
                val response = repository.getSearchDataByKeyword(keyword, userId)
                _keywordSearchData.value = response ?: emptyList() // ✅ 키워드 검색 결과만 업데이트
                println("[SearchViewModel] 키워드 검색 데이터 로드 완료")
            } catch (e: Exception) {
                _error.value = e.message
                println("[SearchViewModel] 키워드 검색 오류 발생: ${e.message}")
            }
        }
    }

    private val _hotTopics = MutableStateFlow<List<HotTopicItem>>(emptyList())
    val hotTopics: StateFlow<List<HotTopicItem>> = _hotTopics.asStateFlow()

    fun fetchHotTopics(limit: Int) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "🔥 fetchHotTopics 실행됨 (limit=$limit)")
            val topics = repository.getHotTopics(limit)
            Log.d("SearchViewModel", "🔥 API 응답 받음: $topics")
            _hotTopics.value = topics
        }
    }

    fun onKeywordClick(keyword: String) {
        viewModelScope.launch {
            repository.getKeywordUrl(keyword)
        }
    }

}
