package com.example.modapjt.components.home

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel

data class FirstKeywordItem(
    val cardId: String,
    val thumbnailUrl: String,
    val title: String,
    val type: String,
    val keywords: List<String>,
    val bookmark: Boolean
)

@Composable
fun FirstKeywordList(navController: NavController, viewModel: SearchViewModel) {
    val keywordSearchData by viewModel.keywordSearchData.collectAsState() // ✅ 변경된 StateFlow 사용

    // 디버깅용 로그 추가
    Log.d("FirstKeywordList", "keywordSearchData size: ${keywordSearchData.size}")

    keywordSearchData.let { items ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (item in items) {
                    BottomThumbnail(
                        cardId = item.cardId,
                        thumbnailUrl = item.thumbnailUrl ?: "",
                        title = item.title ?: "",
                        type = item.type ?: "",
                        keywords = item.keywords ?: emptyList(),
                        bookmark = item.bookmark ?: false,
                        onClick = { cardId ->
                            navController.navigate("cardDetail/${item.cardId}")
                        }
                    )
                }
            }
        }
    }

}
