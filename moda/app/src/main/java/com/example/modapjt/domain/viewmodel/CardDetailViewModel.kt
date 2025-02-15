package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.modapjt.data.repository.CardDetailRepository
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.CardDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardDetailViewModel : ViewModel() {
    private val repository = CardDetailRepository()
    private val cardRepository = CardRepository()

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

    // 즐겨찾기
    fun toggleBookmark(cardId: String) {
        viewModelScope.launch {
            try {
                println("[CardDetailViewModel] toggleBookmark 시작: cardId=$cardId")

                _uiState.value.let { currentState ->
                    if (currentState is CardDetailUiState.Success) {
                        val currentCard = currentState.cardDetail
                        println("[CardDetailViewModel] 현재 북마크 상태: ${currentCard.bookmark}")

                        val result = cardRepository.toggleBookmark(
                            cardId = cardId,
                            isBookmark = !currentCard.bookmark
                        )
                        println("[CardDetailViewModel] 토글 요청 결과: $result")

                        when {
                            result.isSuccess -> {
                                println("[CardDetailViewModel] 토글 성공 - 상태 업데이트")
                                // UI 상태 업데이트
                                _uiState.value = CardDetailUiState.Success(
                                    currentCard.copy(bookmark = !currentCard.bookmark)
                                )
                                println("[CardDetailViewModel] 새로운 북마크 상태: ${!currentCard.bookmark}")
                            }
                            else -> {
                                println("[CardDetailViewModel] 토글 실패: ${result.exceptionOrNull()?.message}")
                                _uiState.value = CardDetailUiState.Error("북마크 토글 실패")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("[CardDetailViewModel] 예외 발생: ${e.message}")
                _uiState.value = CardDetailUiState.Error("북마크 토글 중 오류 발생: ${e.message}")
            }
        }
    }

    // deleteCard 함수 추가
    fun deleteCard(cardIds: List<String>, navController: NavController) {
        viewModelScope.launch {
            try {
                val result = cardRepository.deleteCard(cardIds)
                if (result.isSuccess) {
                    // 삭제 성공 시 이전 화면으로 이동
                    navController.popBackStack()
                } else {
                    _uiState.value = CardDetailUiState.Error("카드 삭제 실패")
                }
            } catch (e: Exception) {
                _uiState.value = CardDetailUiState.Error("카드 삭제 중 오류 발생: ${e.message}")
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
