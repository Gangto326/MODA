package com.example.modapjt.components.bar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.ArrowBack // 뒤로가기 아이콘 사용
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.res.painterResource
import com.example.modapjt.R

@Composable
fun TopBackBarComponent(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 뒤로가기 이모지 추가 (아이콘)
        IconButton(onClick = {
            // 뒤로가기 버튼 클릭 시 이전 페이지로 이동
            navController.navigateUp()
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,  // 뒤로가기 아이콘
                contentDescription = "뒤로가기"
            )
        }

        // 나머지 아이콘들 (모드 로고 부분은 그대로 둠)
        Row {
            repeat(4) {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Button ${it + 1}"
                    )
                }
            }
        }
    }
}
