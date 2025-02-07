package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.model.CardDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = CardRepository()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private val _cardDetail = MutableStateFlow<CardDetail?>(null)
    val cardDetail: StateFlow<CardDetail?> = _cardDetail

    fun loadCards(userId: String, categoryId: Int, page: Int, size: Int) {
        viewModelScope.launch {
            val result = repository.getCards(userId, categoryId, page, size)
            if (result.isSuccess) _cards.value = result.getOrNull() ?: emptyList()
        }
    }

    fun loadCardDetail(cardId: String, userId: String) {
        viewModelScope.launch {
            val result = repository.getCardDetail(cardId, userId)
            if (result.isSuccess) _cardDetail.value = result.getOrNull()
        }
    }
}
