package com.example.modapjt.components.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchKeywordItem(
    keyword: String,
    onDelete: () -> Unit,
    onSearchSubmit: (String) -> Unit
) {
    // ✨ 클릭 상태 추가
    var isPressed by remember { mutableStateOf(false) }

    // ✨ 회색 색상 정의
    val grayColor = MaterialTheme.colorScheme.secondary

    Row(
        modifier = Modifier
            // ✨ 배경색 제거하고 회색 테두리 추가
            .border(
                width = 1.dp,
                color = if (isPressed) MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,  // ✨ 글씨 크기 축소
                fontWeight = FontWeight.Medium,
                color = grayColor  // ✨ 글씨 색상을 회색으로 변경
            ),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isPressed = true  // ✨ 클릭 상태 활성화
                    onSearchSubmit(keyword)
                    isPressed = false  // ✨ 클릭 상태 비활성화
                }
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "✕",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,  // ✨ X 버튼 크기도 축소
                fontWeight = FontWeight.Bold,
                color = grayColor  // ✨ X 버튼도 회색으로 변경
            ),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isPressed = true  // ✨ 클릭 상태 활성화
                    onDelete()
                    isPressed = false  // ✨ 클릭 상태 비활성화
                }
        )
    }
}