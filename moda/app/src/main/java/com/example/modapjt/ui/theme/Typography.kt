package com.example.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.modapjt.ui.theme.robotoFontFamily

// Typography 설정
val customTypography = Typography(
    // 제목 굵은 버전
    titleLarge  = TextStyle(
        fontFamily = robotoFontFamily, // 우리가 만든 FontFamily 적용
        fontWeight = FontWeight.Bold, // 기본은 Normal (Regular)
        fontSize = 22.sp
    ),
    // 소제목( ㅁ 이미지 )
    titleMedium  = TextStyle(
        fontFamily = robotoFontFamily, // 우리가 만든 FontFamily 적용
        fontWeight = FontWeight.Bold, // 기본은 Normal (Regular)
        fontSize = 18.sp
    ),
    // 상단바 카테고리 / 정보 제목
    headlineMedium  = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold, // 제목은 Bold 폰트 사용
        fontSize = 16.sp
    ),

    ///////////////////////////////////////////////////

    // 상단바 type
    headlineSmall  = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal, // 제목은 Bold 폰트 사용
        fontSize = 16.sp
    ),
    // (정렬 버튼 / 더보기 버튼) 블로그( 유튜버 ) 이름 / 정보 내용요약
    bodyMedium   = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal, // 제목은 Bold 폰트 사용
        fontSize = 14.sp
    ),
    // 저장한 날짜 / 작은 카드 키워드
    bodySmall    = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal, // 제목은 Bold 폰트 사용
        fontSize = 12.sp
    ),
)
