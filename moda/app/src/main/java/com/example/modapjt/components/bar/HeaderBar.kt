package com.example.modapjt.components.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.modapjt.R

@Composable
fun HeaderBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxWidth(), //반응형
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Row( //내부 Row 배치
            modifier = Modifier
                .fillMaxWidth() //fillMaxWidth()로 화면 크기에 맞춰 조정됨
                .height(56.dp) // 헤더 높이 일관된 시각적 표현을 위해 dp 값으로 고정
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, //SpaceBetween으로 로고와 아이콘들이 양끝에 적절히 배치됨
            verticalAlignment = Alignment.CenterVertically //화면 크기가 변해도 요소들 간의 간격이 자동으로 조정됨
        ) {
            // 로고
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo", // 비워두면 안됨.
                modifier = Modifier
                    .height(32.dp) // 로고 크기
                    .width(71.dp) // 로고 크기
            )

            // 우측 아이콘들
            Row(
                horizontalArrangement = Arrangement.spacedBy(-5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 알림 아이콘
                IconButton(
                    onClick = { /* 알림 클릭 이벤트 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp) // 아이콘 크기
                    )
                }

                // 메뉴 아이콘
                IconButton(
                    onClick = { /* 메뉴 클릭 이벤트 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp) // 아이콘 크기
                    )
                }
            }
        }
    }
}