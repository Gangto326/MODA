package com.example.modapjt.components.bar

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.modapjt.components.button.TagButton

@Composable
fun NewsDetail(
    title: String,
    date: String,
    selectedTag: String,
    onTagSelected: (String) -> Unit,
    onFontSizeClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // ✅ Material 3 적용
    ) {
        // 카테고리와 날짜
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "카테고리",
                style = MaterialTheme.typography.labelSmall, // ✅ caption → labelSmall
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall, // ✅ caption → labelSmall
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // 제목
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy( // ✅ h6 → titleLarge
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 태그 버튼들과 기능 버튼들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 태그 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagButton(
                    text = "유재석",
                    isSelected = selectedTag == "유재석",
                    onClick = { onTagSelected("유재석") }
                )
                TagButton(
                    text = "연예대상",
                    isSelected = selectedTag == "연예대상",
                    onClick = { onTagSelected("연예대상") }
                )
                TagButton(
                    text = "대상",
                    isSelected = selectedTag == "대상",
                    onClick = { onTagSelected("대상") }
                )
            }

            // 기능 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onFontSizeClick,
                    modifier = Modifier.size(36.dp) // ✅ 버튼 크기 증가
                ) {
                    Text(
                        text = "AA",
                        style = MaterialTheme.typography.labelMedium, // ✅ caption → labelMedium
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp) // ✅ 버튼 크기 증가
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "공유하기",
                        tint = MaterialTheme.colorScheme.onSurface // ✅ 아이콘 색상 변경
                    )
                }
            }
        }
    }
}
