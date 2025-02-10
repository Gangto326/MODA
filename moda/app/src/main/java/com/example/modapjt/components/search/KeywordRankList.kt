package com.example.modapjt.components.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeywordRankList() {
    val keywords = listOf(
        "침착맨", "싸피", "중간발표", "프론트", "개발",
        "밥심", "안드로이드", "코틀린", "컴포즈", "머신러닝"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // 🔽 양쪽 패딩 적용
    ) {
        // 🔽 2개씩 묶어서 한 줄에 출력
        val keywordPairs = keywords.chunked(2)

        keywordPairs.forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 🔽 좌우 균등 배치
            ) {
                pair.forEachIndexed { index, keyword ->
                    Box(
                        modifier = Modifier.weight(1f), // 🔽 양쪽 균등한 크기 유지
                        contentAlignment = Alignment.CenterStart
                    ) {
                        KeywordRankItem(rank = keywordPairs.indexOf(pair) * 2 + (index + 1), keyword = keyword)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp)) // 🔽 행 간격 추가
        }
    }
}
