package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() { // ViewModel 내부에서 Repository 생성
    private val repository = CardRepository() // 직접 CardRepository 인스턴스 생성

    private val _imageCards = MutableStateFlow<List<ImageCard>>(emptyList())
    val imageCards: StateFlow<List<ImageCard>> = _imageCards

    private val _videoCards = MutableStateFlow<List<VideoCard>>(emptyList())
    val videoCards: StateFlow<List<VideoCard>> = _videoCards

    private val _blogCards = MutableStateFlow<List<BlogCard>>(emptyList())
    val blogCards: StateFlow<List<BlogCard>> = _blogCards

    private val _newsCards = MutableStateFlow<List<NewsCard>>(emptyList())
    val newsCards: StateFlow<List<NewsCard>> = _newsCards

    // 카테고리에 맞는 카드 데이터 가져오기
    fun loadCards(userId: String, categoryId: Int) {
        viewModelScope.launch {
            println("[CardViewModel.kt] CardViewModel - API 요청: userId=$userId, categoryId=$categoryId")

            val result = repository.getCards(userId, categoryId, page=1, size=15)
            if (result.isSuccess) {
                val cards = result.getOrNull() ?: emptyList()
                println("[CardViewModel.kt] API 응답 데이터 (카테고리: $categoryId): ${cards.size}개")

                // 데이터 분류 및 변환
                _imageCards.value = cards.filter { it.typeId == 1 }.map { ImageCard(it.thumbnailUrl ?: "") }
                _videoCards.value = cards.filter { it.typeId == 2 }.map { VideoCard(it.title, it.thumbnailUrl ?: "") }
                _blogCards.value = cards.filter { it.typeId == 3 }.map { BlogCard(it.title, it.thumbnailUrl ?: "", it.thumbnailContent ?: "") }
                _newsCards.value = cards.filter { it.typeId == 4 }.map { NewsCard(it.title, it.thumbnailUrl ?: "", it.keywords ?: emptyList()) }
            } else {
                println("[CardViewModel.kt] API 요청 실패: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}

