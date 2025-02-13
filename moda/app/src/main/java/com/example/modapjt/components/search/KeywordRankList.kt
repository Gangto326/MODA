package com.example.modapjt.components.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KeywordRankList(viewModel: SearchViewModel, navController: NavController) {
    val hotTopics by viewModel.hotTopics.collectAsState()

    Log.d("KeywordRankList", "hotTopics 업데이트됨: $hotTopics")

    // ✅ 최초 실행 시 데이터 불러오기
    LaunchedEffect(Unit) {
        Log.d("KeywordRankList", "🔥 fetchHotTopics() 실행 요청")
        viewModel.fetchHotTopics(10)
    }

    if (hotTopics.isEmpty()) {
        Text("데이터를 불러오는 중...", modifier = Modifier.padding(16.dp))
        return
    }

    val leftTopics = hotTopics.take(5) // 1~5
    val rightTopics = hotTopics.drop(5) // 6~10

    // ✅ 오늘 날짜 가져오기 (yyyy.MM.dd 형식)
    val todayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date())

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // ✅ 제목 + 날짜 한 줄에 배치
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // 좌우 정렬
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽: "인기 검색어"
            Text(
                text = "인기 검색어",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                fontWeight = FontWeight.Bold
            )

            // 오른쪽: "2022.01.01 기준"
            Text(
                text = "$todayDate 기준",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, color = Color.Gray),
                fontWeight = FontWeight.Normal
            )
        }

        // ✅ 제목과 리스트 사이 간격 추가
        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 검색어 리스트 UI
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                leftTopics.forEach { topic ->
                    KeywordRankItem(
                        rank = topic.rank,
                        keyword = topic.topic,
                        change = topic.change,
                        navController = navController // ✅ 추가
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                rightTopics.forEach { topic ->
                    KeywordRankItem(
                        rank = topic.rank,
                        keyword = topic.topic,
                        change = topic.change,
                        navController = navController // ✅ 추가
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
