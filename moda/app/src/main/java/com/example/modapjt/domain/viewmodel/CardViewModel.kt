package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = CardRepository()

    private val _uiState = MutableStateFlow<CardUiState>(CardUiState.Loading)
    val uiState: StateFlow<CardUiState> = _uiState

    fun loadCards(userId: String, categoryId: Int) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading
            try {
                val result = repository.getCards(userId, categoryId, page = 1, size = 15)
                if (result.isSuccess) {
                    val cards = result.getOrNull() ?: emptyList()
                    _uiState.value = CardUiState.Success(
                        images = cards.filter { it.typeId == 1 },
                        videos = cards.filter { it.typeId == 2 },
                        blogs = cards.filter { it.typeId == 3 },
                        news = cards.filter { it.typeId == 4 }
                    )
                } else {
                    _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }


    // 카드 삭제 기능 추가
    fun deleteCard(card: Card) {
        viewModelScope.launch {
            val result = repository.deleteCard(card.cardId)

            if (result.isSuccess) {
                val currentState = _uiState.value
                if (currentState is CardUiState.Success) {
                    _uiState.value = currentState.copy(
                        images = currentState.images.filter { it.cardId != card.cardId },
                        videos = currentState.videos.filter { it.cardId != card.cardId },
                        blogs = currentState.blogs.filter { it.cardId != card.cardId },
                        news = currentState.news.filter { it.cardId != card.cardId }
                    )
                }
            } else {
                println("카드 삭제 실패: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}

sealed class CardUiState {
    object Loading : CardUiState()
    data class Success(
        val images: List<Card>,
        val videos: List<Card>,
        val blogs: List<Card>,
        val news: List<Card>
    ) : CardUiState()
    data class Error(val message: String) : CardUiState()
}
