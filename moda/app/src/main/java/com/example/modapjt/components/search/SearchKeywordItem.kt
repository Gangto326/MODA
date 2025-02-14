package com.example.modapjt.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFFFC107), // 🔽 노란색 배경
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.clickable(onClick = {
                onSearchSubmit(keyword)
            }),
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "✕",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            modifier = Modifier.clickable { onDelete() } // 🔽 삭제 기능 추가
        )
    }
}
