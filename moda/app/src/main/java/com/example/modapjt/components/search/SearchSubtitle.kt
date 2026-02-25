package com.example.modapjt.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchSubtitle(
    title: String,
    date: String,
    isDeletable: Boolean = false, // 전체 삭제 가능 여부
    onDeleteClick: (() -> Unit)? = null // 클릭 이벤트 콜백
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = date,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp, // 🔽 글자 크기 줄이기 (14 → 12)
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // 🔽 글자 색 변경
            modifier = if (isDeletable) {
                Modifier.clickable { onDeleteClick?.invoke() } // 클릭 이벤트 처리
            } else Modifier
        )
    }
}
