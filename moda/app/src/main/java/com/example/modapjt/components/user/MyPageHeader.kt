package com.example.modapjt.components.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R

@Composable
fun MyPageHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "마이페이지",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Notification",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null, // 클릭 효과 제거
                        interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                    ) { /* TODO: 알림 클릭 이벤트 */ }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null, // 클릭 효과 제거
                        interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                    ) { /* TODO: 설정 클릭 이벤트 */ }
            )
        }
    }
}
