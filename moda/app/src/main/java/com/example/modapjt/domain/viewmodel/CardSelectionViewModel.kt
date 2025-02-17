package com.example.modapjt.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modapjt.domain.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardSelectionViewModel<T> : ViewModel() {
    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode.asStateFlow()

    private val _selectedCards = MutableStateFlow<Set<String>>(emptySet())  // cardId 기반
    val selectedCards = _selectedCards.asStateFlow()

    fun toggleSelectionMode(enable: Boolean) {
        _isSelectionMode.value = enable
        if (!enable) {
            clearSelection()
        }
    }

    fun toggleCardSelection(card: T) {
        val cardId = (card as? Card)?.cardId  // Card 타입인 경우 cardId 추출
        cardId?.let { id ->
            _selectedCards.update { selected ->
                if (selected.contains(id)) {
                    selected - id
                } else {
                    selected + id
                }
            }
        }
    }

    fun clearSelection() {
        _selectedCards.value = emptySet()
    }

    fun isCardSelected(cardId: String): Boolean {
        return _selectedCards.value.contains(cardId)
    }
}