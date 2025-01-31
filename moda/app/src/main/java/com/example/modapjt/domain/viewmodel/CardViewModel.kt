//package com.example.modapjt.domain.viewmodel
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import com.example.modapjt.domain.model.Card
//import java.time.LocalDateTime
//
//class CardViewModel : ViewModel() {
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getCardData(cardId: String): Card {
//        // API나 데이터베이스에서 실제 데이터를 가져오는 부분
//        val fetchedCard = getCardDataFromAPI(cardId)
//        return fetchedCard
//    }
//
//    // 데이터를 가져오는 함수 (API 호출 모방)
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getCardDataFromAPI(cardId: String): Card {
//        // 실제 데이터가 아닌 더미 데이터를 반환 (API나 데이터베이스에서 가져오는 방식으로 대체)
//        return when (cardId) {
//            "1" -> Card(
//                cardId = "1",
//                boardId = "1",  // 예시: 이 카드에 해당하는 boardId
//                typeId = 1,  // 예시: 카드 유형
//                urlHash = "abc123",  // 예시 URL 해시
//                title = "카드 제목 1",
//                content = "카드 내용 1",
//                isView = true,
//                embedding = listOf(0.23f, 0.12f, -0.47f, 1.56f),
//                createdAt = LocalDateTime.now(),
//                updatedAt = null,
//                deletedAt = null
//            )
//            "2" -> Card(
//                cardId = "2",
//                boardId = "1",
//                typeId = 2,
//                urlHash = "xyz456",
//                title = "카드 제목 2",
//                content = "카드 내용 2",
//                isView = true,
//                embedding = listOf(0.12f, 0.45f, -0.13f, 0.78f),
//                createdAt = LocalDateTime.now(),
//                updatedAt = null,
//                deletedAt = null
//            )
//            else -> Card(
//                cardId = cardId,
//                boardId = "0",  // 기본 fallback boardId
//                typeId = 0,     // 기본 fallback typeId
//                urlHash = null,
//                title = "카드 없음",
//                content = "이 카드에 대한 데이터가 없습니다.",
//                isView = false,
//                embedding = listOf(),
//                createdAt = LocalDateTime.now(),
//                updatedAt = null,
//                deletedAt = null
//            )
//        }
//    }
//}



package com.example.modapjt.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.ModaRepository
import com.example.modapjt.domain.model.Card
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class CardViewModel : ViewModel() {
    private val repository = ModaRepository()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards = _cards.asStateFlow()

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard = _selectedCard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadCards(boardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _cards.value = repository.getDummyCards(boardId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCardDetail(cardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedCard.value = repository.getCardById(cardId)
                if (_selectedCard.value == null) {
                    _error.value = "카드 정보를 찾을 수 없습니다."
                }
            } catch (e: Exception) {
                _error.value = "데이터 로드 중 오류 발생: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
