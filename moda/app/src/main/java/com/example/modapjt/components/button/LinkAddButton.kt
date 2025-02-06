package com.example.modapjt.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LinkAddButton(
    onClick: () -> Unit, // 클릭 이벤트 핸들러
    modifier: Modifier = Modifier  // 커스텀 수정자
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth() // 버튼이 화면의 가로 너비를 꽉 채우도록 설정
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFD700) // 버튼 컬러
            ,disabledContainerColor = Color.Gray // 버튼 비활성화시 ( 일단 : 회색 )
        ),
        shape = RoundedCornerShape(16.dp) // 모서리 둥글게
    ) {
        Text(
            text = "링크 추가하기",
            color = Color.Black
        )
    }
}