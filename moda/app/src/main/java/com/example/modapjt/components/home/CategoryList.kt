package com.example.modapjt.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryList(
    categories: List<String>,
    modifier: Modifier = Modifier // 외부에서 Modifier를 받도록 설정
) {
    Column(
        modifier = modifier.fillMaxWidth() // Modifier 적용
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.take(5).forEach { category ->
                CategoryItem(
                    name = category,
                    onClick = { /* 클릭 이벤트 */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.drop(5).take(5).forEach { category ->
                CategoryItem(
                    name = category,
                    onClick = { /* 클릭 이벤트 */ }
                )
            }
        }
    }
}
