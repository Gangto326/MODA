package com.example.modapjt.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun KeywordRankItem(rank: Int, keyword: String, change: Int, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 랭킹 번호
        Text(
            text = "$rank.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,  // ✨ 글씨 크기 축소
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.width(28.dp)
        )

        // 키워드
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,  // ✨ 글씨 크기 축소
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch(Dispatchers.IO) {
                        val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                        val updatedKeywords = (listOf(keyword) + currentKeywords).distinct().take(10)
                        SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                    }
                    navController.navigate("newSearchCardListScreen/$keyword")
                }
        )

        // ✨ 변화 아이콘 크기 조정 및 스타일 개선
        val changeText = when {
            change == 100 -> "NEW"
            change > 0 -> "▲${change}"  // 화살표와 숫자 붙임
            change < 0 -> "▼${-change}" // 화살표와 숫자 붙임
            else -> "-"
        }

        Text(
            text = changeText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = when {  // ✨ NEW와 화살표 크기 차별화
                    change == 100 -> 11.sp  // NEW는 더 작게
                    else -> 12.sp  // 화살표는 조금 더 크게
                },
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.width(40.dp),  // ✨ 고정 너비 설정
            textAlign = TextAlign.End,  // ✨ 오른쪽 정렬
            color = when {
                change == 100 ->Color(0xFFFFCC80)
                change > 0 -> Color.Red
                change < 0 -> Color.Blue
                else -> Color.Gray
            }
        )
    }
}