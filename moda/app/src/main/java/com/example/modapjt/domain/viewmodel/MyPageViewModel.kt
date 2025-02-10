//package com.example.modapjt.domain.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import com.example.modapjt.data.api.RetrofitInstance
//import com.example.modapjt.domain.model.User
//import com.example.modapjt.data.dto.response.toDomainModel // ✅ 변환 함수 import 추가
//
//class UserViewModel : ViewModel() {
//    private val _user = MutableStateFlow<User?>(null)
//    val user: StateFlow<User?> get() = _user
//
//    fun fetchUser(userId: String) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.userApi.getUserProfile(userId) // ✅ API 호출
//                _user.value = response.toDomainModel() // ✅ 데이터 변환 후 저장
//            } catch (e: Exception) {
//                _user.value = null
//            }
//        }
//    }
//}
//
//
//
