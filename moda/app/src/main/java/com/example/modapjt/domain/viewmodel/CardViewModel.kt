package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = CardRepository()
    private val _uiState = MutableStateFlow<CardUiState>(CardUiState.Loading)
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    // ✅ 페이징 관련 상태
    private var currentPage = 1
    private var isLoading = false
    private var hasNextPage = true

    private val _loadingMore = MutableStateFlow(false)
    val loadingMore: StateFlow<Boolean> = _loadingMore.asStateFlow()

    // ✅ 전체탭 카드 로드 (페이징 없음)
    private fun loadAllTabCards(userId: String, query: String, categoryId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getAllTabCards(userId, query, categoryId)
                if (result.isSuccess) {
                    val cards = result.getOrNull() ?: emptyList()
                    _uiState.value = CardUiState.Success(
                        images = cards.filter { it.typeId == 4 },
                        blogs = cards.filter { it.typeId == 2 },
                        videos = cards.filter { it.typeId == 1 },
                        news = cards.filter { it.typeId == 3 }
                    )
                } else {
                    _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    // ✅ 특정탭 카드 로드 (페이징 포함)
    private fun loadTabCards(
        userId: String,
        query: String,
        categoryId: Int,
        typeId: Int,
        sortDirection: String,
        isLoadMore: Boolean = false
    ) {
        if (isLoading || (!isLoadMore && !hasNextPage)) return

        viewModelScope.launch {
            isLoading = true
            _loadingMore.value = isLoadMore

            try {
                val page = if (isLoadMore) currentPage + 1 else 1
                val result = repository.getTabCards(userId, query, categoryId, typeId, page, sortDirection)

                if (result.isSuccess) {
                    val (cards, hasNext) = result.getOrNull()!!
                    hasNextPage = hasNext

                    if (isLoadMore) {
                        currentPage++
                        appendCards(cards, typeId)
                    } else {
                        currentPage = 1
                        setInitialCards(cards, typeId)
                    }
                } else {
                    _uiState.value = CardUiState.Error("데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            } finally {
                isLoading = false
                _loadingMore.value = false
            }
        }
    }

    // ✅ 공개 로드 함수
    fun loadCards(userId: String, categoryId: Int, selectedTab: String, sortDirection: String, isLoadMore: Boolean = false) {
        if (selectedTab == "전체") {
            loadAllTabCards(userId, "", categoryId)
        } else {
            val typeId = getTypeIdForTab(selectedTab)
            loadTabCards(userId, "", categoryId, typeId, sortDirection, isLoadMore)
        }
    }

    // ✅ 검색용 로드 함수
    fun loadSearchCards(userId: String, query: String, selectedTab: String, sortDirection: String, isLoadMore: Boolean = false) {
        if (selectedTab == "전체") {
            loadAllTabCards(userId, query, 0)
        } else {
            val typeId = getTypeIdForTab(selectedTab)
            loadTabCards(userId, query, 0, typeId, sortDirection, isLoadMore)
        }
    }

    // ✅ 즐겨찾기 카드 로드
    fun loadBookmarkedCards(userId: String, typeId: Int, sortDirection: String, isLoadMore: Boolean = false) {
        if (isLoading || (!isLoadMore && !hasNextPage)) return

        viewModelScope.launch {
            isLoading = true
            _loadingMore.value = isLoadMore

            try {
                val page = if (isLoadMore) currentPage + 1 else 1
                val result = repository.getTabBookMarkCards(userId, typeId, page, 15, sortDirection)

                if (result.isSuccess) {
                    val (cards, hasNext) = result.getOrNull()!!
                    hasNextPage = hasNext

                    if (isLoadMore) {
                        currentPage++
                        appendCards(cards, typeId)
                    } else {
                        currentPage = 1
                        setInitialCards(cards, typeId)
                    }
                } else {
                    _uiState.value = CardUiState.Error("즐겨찾기를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            } finally {
                isLoading = false
                _loadingMore.value = false
            }
        }
    }

    // 즐겨찾기 전체탭
    fun loadAllBookmarkedCards(userId: String) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading
            try {
                // ✅ 전체 데이터 가져오기
                val result = repository.getAllTabBookMarkCards(userId)

                if (result.isSuccess) {
                    val cards = result.getOrNull() ?: emptyList()
                    _uiState.value = CardUiState.Success(
                        images = cards.filter { it.typeId == 4 },
                        blogs = cards.filter { it.typeId == 2 },
                        news = cards.filter { it.typeId == 3 },
                        videos = cards.filter { it.typeId == 1 }
                    )
                } else {
                    _uiState.value = CardUiState.Error("전체 데이터를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = CardUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }


    // ✅ 페이지네이션 리셋
    fun resetPagination() {
        currentPage = 1
        hasNextPage = true
        isLoading = false
        _loadingMore.value = false
    }

    // ✅ 카드 추가/삭제 함수
    private fun appendCards(newCards: List<Card>, typeId: Int) {
        val currentState = _uiState.value as? CardUiState.Success ?: return
        _uiState.value = CardUiState.Success(
            images = if (typeId == 4) currentState.images + newCards else currentState.images,
            blogs = if (typeId == 2) currentState.blogs + newCards else currentState.blogs,
            videos = if (typeId == 1) currentState.videos + newCards else currentState.videos,
            news = if (typeId == 3) currentState.news + newCards else currentState.news
        )
    }

    private fun setInitialCards(cards: List<Card>, typeId: Int) {
        _uiState.value = CardUiState.Success(
            images = if (typeId == 4) cards else emptyList(),
            blogs = if (typeId == 2) cards else emptyList(),
            videos = if (typeId == 1) cards else emptyList(),
            news = if (typeId == 3) cards else emptyList()
        )
    }

    private fun getTypeIdForTab(selectedTab: String): Int = when (selectedTab) {
        "이미지" -> 4
        "블로그" -> 2
        "뉴스" -> 3
        "동영상" -> 1
        else -> 0
    }

    // 기존 deleteCard 함수는 유지
    fun deleteCard(cardIds: List<String>) {
        viewModelScope.launch {
            val result = repository.deleteCard(cardIds)
            if (result.isSuccess) {
                val currentState = _uiState.value
                if (currentState is CardUiState.Success) {
                    _uiState.value = currentState.copy(
                        images = currentState.images.filterNot { it.cardId in cardIds },
                        videos = currentState.videos.filterNot { it.cardId in cardIds },
                        blogs = currentState.blogs.filterNot { it.cardId in cardIds },
                        news = currentState.news.filterNot { it.cardId in cardIds }
                    )
                }
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