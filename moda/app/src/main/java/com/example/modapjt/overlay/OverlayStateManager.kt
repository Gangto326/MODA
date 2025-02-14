package com.example.modapjt.overlay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object OverlayStateManager {
    private val _isOverlayActive = MutableStateFlow(false)
    val isOverlayActive = _isOverlayActive.asStateFlow()

    fun setOverlayActive(active: Boolean) {
        _isOverlayActive.value = active
    }
}