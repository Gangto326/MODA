package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.model.CardDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//
//class CardViewModel : ViewModel() {
//    private val repository = CardRepository()
//
//    private val _cards = MutableStateFlow<List<Card>>(emptyList()) // 카드 리스트
//    val cards = _cards.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false) // 로딩 상태
//    val isLoading = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null) // 에러 메시지
//    val error = _error.asStateFlow()
//
//    fun loadCardList(
//        userId: String,
//        boardId: String
//    ) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            val result = repository.getCardList(userId, boardId)
//            result.onSuccess {
//                _cards.value = it
//            }.onFailure {
//                _error.value = it.message
//                Log.e("CardViewModel", "Error loading card list: ${it.message}")
//            }
//
//            _isLoading.value = false
//        }
//    }
//}


class CardViewModel : ViewModel() {
    private val repository = CardRepository()

    // 카드 목록을 위한 상태
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards = _cards.asStateFlow()

    // 카드 상세 정보를 위한 상태
    private val _cardDetail = MutableStateFlow<CardDetail?>(null)
    val cardDetail = _cardDetail.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // 카드 목록 로드
    fun loadCardList(userId: String, boardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getCardList(userId, boardId)
                .onSuccess { _cards.value = it }
                .onFailure { _error.value = it.message }

            _isLoading.value = false
        }
    }

    // 카드 상세 정보 로드
    fun loadCardDetail(userId: String, cardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getCardDetail(userId, cardId)
                .onSuccess { _cardDetail.value = it }
                .onFailure { _error.value = it.message }

            _isLoading.value = false
        }
    }
}