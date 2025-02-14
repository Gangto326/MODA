package com.example.modapjt.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.width(28.dp)
        )

        // 키워드
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    // 최근 검색어에 저장
                    coroutineScope.launch(Dispatchers.IO) {
                        val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                        val updatedKeywords = (listOf(keyword) + currentKeywords).distinct().take(10)
                        SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                    }
                    // 검색 결과 화면으로 이동
                    navController.navigate("newSearchCardListScreen/$keyword")
                }
        )

        // 변화 아이콘
        val changeSymbol = when {
            change == 100 -> "NEW"
            change > 0 -> "▲ $change"
            change < 0 -> "▼ ${-change}"
            else -> "━"
        }

        Text(
            text = changeSymbol,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            color = when {
                change == 100 -> Color.Green
                change > 0 -> Color.Red
                change < 0 -> Color.Blue
                else -> Color.Gray
            }
        )
    }
}