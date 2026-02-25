package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.UserStatusResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//class UserViewModel : ViewModel() {
//    private val searchRepository = SearchRepository(RetrofitInstance.searchApi) // ✅ SearchRepository 연결
//
//    private val _user = MutableStateFlow<User?>(null)
//    val user: StateFlow<User?> get() = _user
//
//    private val _interestKeywords = MutableStateFlow<List<String>>(emptyList())
//    val interestKeywords: StateFlow<List<String>> = _interestKeywords
//
//    // 사용자 정보 가져오기
//    fun fetchUser() {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.userApi.getUserStatus()
//                _user.value = response
//            } catch (e: Exception) {
//                _user.value = null
//            }
//        }
//    }
//
//}
class UserViewModel : ViewModel() {
    private val _userStatus = MutableStateFlow<UserStatusResponse?>(null)
    val userStatus: StateFlow<UserStatusResponse?> = _userStatus.asStateFlow()

    fun fetchUserStatus() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUserStatus()
                _userStatus.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error fetching user status", e)
            }
        }
    }
}