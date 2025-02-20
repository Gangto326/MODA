@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.modapjt.components.cardtab

import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.viewmodel.CardSelectionViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> SwipableCardList(
    cards: List<T>,
    onDelete: (List<T>) -> Unit,  // 여러 카드 삭제 가능하도록 수정
    onCardDetail: (T) -> Unit,
    selectionViewModel: CardSelectionViewModel<T>,
    enableLongPress: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable (T, Boolean) -> Unit  // 선택 상태를 표시하기 위해 Boolean 추가
) {
    val scope = rememberCoroutineScope() // 추가

    val offsetMap = remember(cards) { // cards를 key로 추가
        mutableStateMapOf<T, MutableState<Float>>().apply {
            cards.forEach { card ->
                this[card] = mutableStateOf(0f)
            }
        }
    }

    val deleteStateMap = remember(cards) { // cards를 key로 추가
        mutableStateMapOf<T, MutableState<Boolean>>().apply {
            cards.forEach { card ->
                this[card] = mutableStateOf(false)
            }
        }
    }

    val isSelectionMode by selectionViewModel.isSelectionMode.collectAsState()
    val selectedCardIds by selectionViewModel.selectedCards.collectAsState()

    // 진동
    val haptics = LocalHapticFeedback.current

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isSelectionMode) 64.dp else 0.dp)
        ) {
            cards.forEach { card ->
                // 현재 화면에 표시될 카드인 경우에만 스와이프 기능 활성화
                val isCurrentCard = cards.contains(card)
                val offsetX = if (isCurrentCard) {
                    offsetMap.getOrPut(card) { mutableStateOf(0f) }
                } else {
                    remember { mutableStateOf(0f) }
                }
                val showDeleteButton = deleteStateMap.getOrPut(card) { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .pointerInput(isSelectionMode) {
                            detectTapGestures(
                                onTap = {
                                    if (isSelectionMode) {
                                        selectionViewModel.toggleCardSelection(card)  // card.id 가정
                                    } else {
                                        onCardDetail(card)
                                    }
                                },
                                onLongPress = {
                                    if (!isSelectionMode && enableLongPress) {
                                        // 진동 효과 추가
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectionViewModel.toggleSelectionMode(true)
                                        selectionViewModel.toggleCardSelection(card)  // card.id 가정
                                    }
                                }
                            )
                        }
                        .let { // card를 key로 추가
                            if (isCurrentCard && !isSelectionMode) {
                                it.pointerInput(Unit) {
                                    detectHorizontalDragGestures(
                                        onDragEnd = {
                                            scope.launch {
                                                if (offsetX.value < -100f) {
                                                    offsetX.value = -140f
                                                    showDeleteButton.value = true
                                                } else {
                                                    offsetX.value = 0f
                                                    showDeleteButton.value = false
                                                }
                                                animate(offsetX.value, 0f) { value, _ ->
                                                    offsetX.value = value
                                                }
                                            }
                                        }
                                    ) { change, dragAmount ->
                                        change.consume()
                                        offsetX.value = (offsetX.value + dragAmount)
                                            .coerceIn(-140f, 0f)
                                    }
                                }
                            } else {
                                it
                            }
                        }
                ) {
                    // 선택 표시 오버레이
                    if (card is Card && selectedCardIds.contains(card.cardId)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                                .zIndex(0.5f)
                        )
                    }
                    // 카드 UI
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    ) {
                        content(
                            card,
                            card is Card && selectedCardIds.contains((card as Card).cardId)
                        )
                    }

                    // 삭제 버튼
                    if (showDeleteButton.value) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .width(80.dp)
                                .height(80.dp)
                                .background(MaterialTheme.colorScheme.onError, shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(
                                onClick = {
                                    scope.launch { // coroutineScope 사용
                                        // 삭제할 때 상태를 초기화하여 다른 카드가 영향을 받지 않도록 처리
                                        offsetMap.remove(card)
                                        deleteStateMap.remove(card)
                                        onDelete(listOf(card))

                                        // 모든 카드들의 상태를 초기화
                                        offsetMap.keys.forEach { remainingCard ->
                                            offsetMap[remainingCard]?.value = 0f
                                        }
                                        deleteStateMap.keys.forEach { remainingCard ->
                                            deleteStateMap[remainingCard]?.value = false
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    "삭제",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}