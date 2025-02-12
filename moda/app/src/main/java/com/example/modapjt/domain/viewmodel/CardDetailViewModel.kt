package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardDetailRepository
import com.example.modapjt.domain.model.CardDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardDetailViewModel : ViewModel() {
    private val repository = CardDetailRepository()

    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState

    // 카드 상세 정보 불러오기
    fun loadCardDetail(userId: String, cardId: String) {  // ✅ cardId 타입 String 유지
        viewModelScope.launch {
            _uiState.value = CardDetailUiState.Loading
            try {
                val result = repository.getCardDetail(userId, cardId)  // ✅ 수정된 Repository 사용
                if (result.isSuccess) {
                    val cardDetail = result.getOrNull()
                    if (cardDetail != null) {
                        _uiState.value = CardDetailUiState.Success(cardDetail)  // ✅ 반환 타입 일치
                    } else {
                        _uiState.value = CardDetailUiState.Error("카드 상세정보가 없습니다.")
                    }
                } else {
                    _uiState.value = CardDetailUiState.Error("데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardDetailUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}

// UI 상태 정의
sealed class CardDetailUiState {
    object Loading : CardDetailUiState()
    data class Success(val cardDetail: CardDetail) : CardDetailUiState()  // ✅ List<Card> → CardDetail 변경
    data class Error(val message: String) : CardDetailUiState()
}
