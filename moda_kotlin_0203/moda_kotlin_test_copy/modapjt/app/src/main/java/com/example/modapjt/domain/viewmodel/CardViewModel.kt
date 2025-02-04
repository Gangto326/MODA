package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = CardRepository()

    private val _cards = MutableStateFlow<List<Card>>(emptyList()) // 카드 리스트
    val cards = _cards.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // 로딩 상태
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null) // 에러 메시지
    val error = _error.asStateFlow()

    fun loadCardList(
        userId: String,
        boardId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getCardList(userId, boardId)
            result.onSuccess {
                _cards.value = it
            }.onFailure {
                _error.value = it.message
                Log.e("CardViewModel", "Error loading card list: ${it.message}")
            }

            _isLoading.value = false
        }
    }
}
