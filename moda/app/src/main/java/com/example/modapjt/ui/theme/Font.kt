package com.example.modapjt.ui.theme


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.modapjt.R  // 패키지 경로 맞게 변경!

// 3개의 폰트를 포함하는 FontFamily 생성
val robotoFontFamily = FontFamily(
    Font(R.font.roboto_medium, FontWeight.Normal), // 일반 폰트
    Font(R.font.roboto_bold, FontWeight.Bold),      // 굵은 폰트
    Font(R.font.roboto_light, FontWeight.Light)     // 얇은 폰트
)
