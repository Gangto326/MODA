//package com.example.modapjt.domain.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.modapjt.data.repository.BoardRepository
//import com.example.modapjt.domain.model.Board
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class BoardViewModel : ViewModel() {
//    private val repository = BoardRepository()
//
//    private val _boards = MutableStateFlow<List<Board>>(emptyList()) // 보드 리스트
//    val boards = _boards.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false) // 로딩 상태
//    val isLoading = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null) // 에러 메시지
//    val error = _error.asStateFlow()
//
//    // API에서 보드 리스트 가져오기
//    fun loadBoardList(userId: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            val result = repository.getBoardList(userId)
//            result.onSuccess {
//                _boards.value = it // UI에 반영
//            }.onFailure {
//                _error.value = it.message
//                Log.e("BoardViewModel", "Error loading board list: ${it.message}")
//            }
//
//            _isLoading.value = false
//        }
//    }
//}
