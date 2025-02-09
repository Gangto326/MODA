@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.modapjt.components.cardtab

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun <T> SwipableCardList(
    cards: List<T>,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    val offsetMap = remember { mutableStateMapOf<T, MutableState<Float>>() }
    val deleteStateMap = remember { mutableStateMapOf<T, MutableState<Boolean>>() }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        cards.forEach { card ->
            val offsetX = offsetMap.getOrPut(card) { mutableStateOf(0f) }
            val showDeleteButton = deleteStateMap.getOrPut(card) { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX.value < -100f) {
                                    offsetX.value = -140f
                                    showDeleteButton.value = true
                                } else {
                                    offsetX.value = 0f
                                    showDeleteButton.value = false
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
                        TextButton(onClick = {
                            // 삭제할 때 상태를 초기화하여 다른 카드가 영향을 받지 않도록 처리
                            offsetMap.remove(card)
                            deleteStateMap.remove(card)
                            onDelete(card)

                            // 모든 카드들의 상태를 초기화하여 정상적으로 드래그 가능하도록 설정
                            offsetMap.keys.forEach { remainingCard ->
                                offsetMap[remainingCard]?.value = 0f
                            }
                            deleteStateMap.keys.forEach { remainingCard ->
                                deleteStateMap[remainingCard]?.value = false
                            }
                        }) {
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
