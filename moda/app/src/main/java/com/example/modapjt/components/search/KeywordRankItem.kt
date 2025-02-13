package com.example.modapjt.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun KeywordRankItem(rank: Int, keyword: String, change: Int, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 랭킹 번호 (고정 너비)
        Text(
            text = "$rank.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.width(28.dp) // ✅ 숫자 + 점(.) 포함한 공간 확보
        )

        // 🔹 키워드 (클릭 시 네비게이션 이동)
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .weight(1f) // ✅ 왼쪽 정렬 (가장 넓은 공간 차지)
                .clickable {
                    navController.navigate("newSearchCardListScreen/$keyword") // ✅ 클릭 시 이동
                }
        )

        // 🔹 변화 아이콘 (오른쪽 정렬)
        val changeSymbol = when {
            change == 100 -> "NEW"  // 🔹 100이면 "NEW"
            change > 0 -> "▲ $change"  // 🔹 양수면 위쪽 화살표
            change < 0 -> "▼ ${-change}"  // 🔹 음수면 아래쪽 화살표
            else -> "━"  // 🔹 0이면 "-"
        }

        Text(
            text = changeSymbol,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            color = when {
                change == 100 -> Color.Green  // 🔹 NEW는 초록색
                change > 0 -> Color.Red  // 🔹 상승은 빨간색
                change < 0 -> Color.Blue  // 🔹 하락은 파란색
                else -> Color.Gray  // 🔹 변동 없음
            }
        )
    }
}
