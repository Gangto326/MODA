package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomainModel
import com.example.modapjt.data.repository.SearchRepository
import com.example.modapjt.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val searchRepository = SearchRepository(RetrofitInstance.searchApi) // ✅ SearchRepository 연결

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _interestKeywords = MutableStateFlow<List<String>>(emptyList())
    val interestKeywords: StateFlow<List<String>> = _interestKeywords

    // ✅ 사용자 정보 가져오기
    fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUserProfile()
                _user.value = response.toDomainModel()
            } catch (e: Exception) {
                _user.value = null
            }
        }
    }

    // ✅ 관심 키워드 가져오기
//    fun fetchInterestKeywords(keyword: String) {
//        viewModelScope.launch {
//            try {
//                Log.d("UserViewModel", "Fetching interest keywords for: $keyword") // ✅ 로그 추가
//                val keywordsData = searchRepository.getInterestKeywords(keyword)
//                Log.d("UserViewModel", "Received keywords: $keywordsData") // ✅ 받은 데이터 출력
//                _interestKeywords.value = keywordsData
//            } catch (e: Exception) {
//                Log.e("UserViewModel", "Error fetching keywords: ${e.message}") // ✅ 에러 로그 추가
//            }
//        }
//    }

}
