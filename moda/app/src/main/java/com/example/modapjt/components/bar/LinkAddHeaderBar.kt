package com.example.modapjt.components.bar

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LinkAddHeaderBar(
    value: String, // 텍스트 값
    onValueChange: (String) -> Unit, // 텍스트 변경시 호출될 함수
    modifier: Modifier = Modifier // 레이아웃 수정자 (기본값은 Modifier)
) {
    TextField(
        value = value, // 현재 입력된 값
        onValueChange = onValueChange, // 텍스트가 변경될 때 호출되는 함수
        modifier = modifier
            .fillMaxWidth() // 입력창이 화면 너비를 꽉 채우도록 설정
            .height(56.dp) // 높이를 56dp로 설정
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))  // 테두리 두께 1dp, 색상은 회색
            .padding(horizontal = 16.dp) // 양옆 여백 16dp설정
//            .padding(vertical = 8.dp) // 위,아래 여백 8dp설정
            .horizontalScroll(rememberScrollState()), // ✅ 긴 URL일 경우 가로 스크롤 가능하게 설정


        placeholder = { Text("링크를 입력하세요") }, // 입력창에 기본적으로 보이는 텍스트

        singleLine = true, // ✅ 한 줄 입력으로 제한 (줄바꿈 방지)
        visualTransformation = VisualTransformation.None, // ✅ URL 입력 시 변형 없이 그대로 표시


        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White, // 포커스가 없을 때 배경색
            focusedContainerColor = Color.White, // 포커스가 있을 때 배경색
            unfocusedIndicatorColor = Color.Transparent, // 포커스가 없을 때 아래쪽 인디케이터 색상 (투명)
            focusedIndicatorColor = Color.Transparent // 포커스가 있을 때 아래쪽 인디케이터 색상 (투명)
        ),
        shape = RoundedCornerShape(16.dp) // 입력창 테두리 둥글게 처리
    )
}