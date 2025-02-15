@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.modapjt.components.cardtab

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipableCardList(
    cards: List<T>,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        cards.forEach { card ->
            val offsetX = offsetMap.getOrPut(card) { mutableStateOf(0f) }
            val showDeleteButton = deleteStateMap.getOrPut(card) { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(card) { // card를 key로 추가
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch { // coroutineScope 사용
                                    if (offsetX.value < -100f) {
                                        offsetX.value = -140f
                                        showDeleteButton.value = true
                                    } else {
                                        offsetX.value = 0f
                                        showDeleteButton.value = false
                                    }
                                }
                            }
                        ) { _, dragAmount ->
                            offsetX.value = (offsetX.value + dragAmount).coerceIn(-140f, 0f)
                        }
                    }
            ) {
                // 삭제 버튼
                if (showDeleteButton.value) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(80.dp)
                            .height(80.dp)
                            .background(Color.Red, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                scope.launch { // coroutineScope 사용
                                    // 삭제할 때 상태를 초기화하여 다른 카드가 영향을 받지 않도록 처리
                                    offsetMap.remove(card)
                                    deleteStateMap.remove(card)
                                    onDelete(card)

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
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // 카드 UI
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                ) {
                    content(card)
                }
            }
        }
    }
}