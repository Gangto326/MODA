package com.example.modapjt.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.modapjt.R

@Composable
fun CategoryHeaderBar(modifier: Modifier = Modifier, categoryName: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) , // 높이 설정
//            .background(Color.White),  // 배경색 설정
        contentAlignment = Alignment.Center
    ) {
        // 왼쪽 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    modifier = Modifier.size(15.dp),
                    contentDescription = "뒤로가기",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // 중앙 타이틀 (카테고리)
        Text(
            text = categoryName, // 선택한 카테고리 반영
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
