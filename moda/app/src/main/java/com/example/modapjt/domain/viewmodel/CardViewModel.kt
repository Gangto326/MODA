package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = CardRepository()

    private val _uiState = MutableStateFlow<CardUiState>(CardUiState.Loading)
    val uiState: StateFlow<CardUiState> = _uiState

    private var sortDirection = MutableStateFlow("DESC") // 기본 정렬 : 최신순

//    fun loadCards(userId: String, categoryId: Int) {
//        viewModelScope.launch {
//            _uiState.value = CardUiState.Loading
//            try {
//                val result = repository.getCards(userId, categoryId, page = 1, size = 15)
//                if (result.isSuccess) {
//                    val cards = result.getOrNull() ?: emptyList()
//                    _uiState.value = CardUiState.Success(
//                        images = cards.filter { it.typeId == 1 },
//                        videos = cards.filter { it.typeId == 2 },
//                        blogs = cards.filter { it.typeId == 3 },
//                        news = cards.filter { it.typeId == 4 }
//                    )
//                } else {
//                    _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
//                }
//            } catch (e: Exception) {
//                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
//            }
//        }
//    }
// ✅ 전체탭 및 특정탭 카드 리스트 불러오기
//fun loadCards(userId: String, categoryId: Int, selectedTab: String) {
//    viewModelScope.launch {
//        _uiState.value = CardUiState.Loading
//        try {
//            val result = if (selectedTab == "전체") {
//                repository.getAllTabCards(userId, "", categoryId) // ✅ api/search/main 호출
//            } else {
//                val typeId = when (selectedTab) {
//                    "이미지" -> 1
//                    "블로그" -> 2
//                    "뉴스" -> 3
//                    "영상" -> 4
//                    else -> 0
//                }
//                repository.getTabCards(userId, "", categoryId, typeId) // ✅ api/search 호출
//            }
//
//            if (result.isSuccess) {
//                val cards = result.getOrNull() ?: emptyList()
//                _uiState.value = CardUiState.Success(
//                    images = cards.filter { it.typeId == 1 },
//                    videos = cards.filter { it.typeId == 2 },
//                    blogs = cards.filter { it.typeId == 3 },
//                    news = cards.filter { it.typeId == 4 }
//                )
//            } else {
//                _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
//            }
//        } catch (e: Exception) {
//            _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
//        }
//    }
//}

    // 카드 리스트 불러오기 (정렬 포함)
    fun loadCards(userId: String, categoryId: Int, selectedTab: String, sortDirection: String) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading
            try {
                println("[CardViewModel] $selectedTab 탭 데이터 로드 시작 (정렬: $sortDirection)")

                val result = if (selectedTab == "전체") {
                    repository.getAllTabCards(userId, "", categoryId)
                } else {
                    val typeId = when (selectedTab) {
                        "이미지" -> 4
                        "블로그" -> 2
                        "뉴스" -> 3
                        "동영상" -> 1
                        else -> 0
                    }
                    repository.getTabCards(userId, "", categoryId, typeId, sortDirection) // 정렬 추가
                }

                if (result.isSuccess) {
                    val cards = result.getOrNull() ?: emptyList()
                    println("[CardViewModel] 데이터 개수: ${cards.size}")

                    if (selectedTab == "전체") {
                        val images = cards.filter { it.typeId == 4 }  // 이미지 typeId = 4
                        val blogs = cards.filter { it.typeId == 2 }
                        val news = cards.filter { it.typeId == 3 }
                        val videos = cards.filter { it.typeId == 1 }  // 동영상 typeId = 1


                        println("[CardViewModel] 변환된 이미지 개수: ${images.size}")
                        println("[CardViewModel] 변환된 블로그 개수: ${blogs.size}")
                        println("[CardViewModel] 변환된 뉴스 개수: ${news.size}")
                        println("[CardViewModel] 변환된 영상 개수: ${videos.size}")

                        _uiState.value = CardUiState.Success(
                            images = images,
                            blogs = blogs,
                            news = news,
                            videos = videos
                        )
                    } else {
                        _uiState.value = CardUiState.Success(
                            images = if (selectedTab == "이미지") cards else emptyList(),
                            blogs = if (selectedTab == "블로그") cards else emptyList(),
                            news = if (selectedTab == "뉴스") cards else emptyList(),
                            videos = if (selectedTab == "동영상") cards else emptyList()
                        )
                    }
                } else {
                    println("[CardViewModel] 데이터 로드 실패")
                    _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                println("[CardViewModel] 예외 발생: ${e.message}")
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }



    // ✅ 새로운 방식: 키워드 검색을 기반으로 카드 리스트 로드
    fun loadSearchCards(userId: String, query: String, selectedTab: String, sortDirection: String) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading
            try {
                println("[CardViewModel] 검색어 '$query' 로 데이터 로드 시작 (정렬: $sortDirection)")

                val result = if (selectedTab == "전체") {
                    repository.getAllTabCards(userId, query, 0) // ✅ 검색어 기반 API 호출
                } else {
                    val typeId = when (selectedTab) {
                        "이미지" -> 4
                        "블로그" -> 2
                        "뉴스" -> 3
                        "동영상" -> 1
                        else -> 0
                    }
                    repository.getTabCards(userId, query, 0, typeId, sortDirection)
                }

                if (result.isSuccess) {
                    val cards = result.getOrNull() ?: emptyList()
                    _uiState.value = createCardUiState(cards, selectedTab)
                } else {
                    _uiState.value = CardUiState.Error("검색 데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    // ✅ 공통 함수: UI 상태를 카드 데이터로 변환 (isMine 체크하여 배경색 변경)
//    private fun createCardUiState(cards: List<Card>, selectedTab: String): CardUiState.Success {
//        val updatedCards = cards.map { card ->
//            card.copy(backgroundColor = if (!card.isMine) "gray" else "white") // ✅ isMine=false이면 회색 배경
//        }
//
//        return CardUiState.Success(
//            images = if (selectedTab == "전체") updatedCards.filter { it.typeId == 4 } else emptyList(),
//            blogs = if (selectedTab == "전체") updatedCards.filter { it.typeId == 2 } else emptyList(),
//            news = if (selectedTab == "전체") updatedCards.filter { it.typeId == 3 } else emptyList(),
//            videos = if (selectedTab == "전체") updatedCards.filter { it.typeId == 1 } else emptyList()
//        )
//    }
    private fun createCardUiState(cards: List<Card>, selectedTab: String): CardUiState.Success {
        return CardUiState.Success(
            images = if (selectedTab == "전체") cards.filter { it.typeId == 4 } else emptyList(),
            blogs = if (selectedTab == "전체") cards.filter { it.typeId == 2 } else emptyList(),
            news = if (selectedTab == "전체") cards.filter { it.typeId == 3 } else emptyList(),
            videos = if (selectedTab == "전체") cards.filter { it.typeId == 1 } else emptyList()
        )
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
