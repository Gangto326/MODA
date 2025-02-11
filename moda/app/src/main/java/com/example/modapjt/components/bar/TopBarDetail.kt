package com.example.modapjt.components.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.modapjt.R // ✅ ic_star, ic_back, ic_more 아이콘을 리소스에서 가져오기

@Composable
fun TopBarDetail(navController: NavController) { // ✅ NavController 추가
    Row(
        modifier = Modifier
            .fillMaxWidth() // ✅ 전체 너비 확보
            .height(56.dp)
            .background(color = Color.White)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ✅ 왼쪽 뒤로 가기 아이콘 (이전 화면으로 이동)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.navigateUp() }) { // ✅ 이전 화면으로 이동
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(24.dp) // ✅ 아이콘 크기 24.dp
                )
            }
        }

        // ✅ 중앙 제목 정렬
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "뉴스",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // ✅ 오른쪽 아이콘 그룹
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: 즐겨찾기 기능 추가 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "즐겨찾기",
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = { /* TODO: 메뉴 기능 추가 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "더보기",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
