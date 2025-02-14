package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Category

@Composable
fun CategoryItem(category: Category, navController: NavController) {
    val categoryNameMap = mapOf(
        "All" to "전체",
        "Trends" to "트렌드",
        "Entertainment" to "오락",  // "엔터테인먼트" → "오락" 변경
        "Finance" to "금융",
        "Travel" to "여행",
        "Food" to "음식",
        "IT" to "IT",
        "Design" to "디자인",
        "Society" to "사회",
        "Health" to "건강"
    )


    val iconVector = when (category.category) {
        "All" -> Icons.Default.AccountBox
        "Trends" -> Icons.Default.Call
        "Entertainment" -> Icons.Default.Info
        "Finance" -> Icons.Default.Star
        "Travel" -> Icons.Default.ThumbUp
        "Food" -> Icons.Default.Menu
        "IT" -> Icons.Default.Lock
        "Design" -> Icons.Default.Face
        "Society" -> Icons.Default.Email
        "Health" -> Icons.Default.Create
        else -> Icons.Default.Done
    }

    val categoryName = categoryNameMap[category.category] ?: category.category

    Column(
        modifier = Modifier
            .padding(4.dp)  // 패딩 값 축소
            .clickable { navController.navigate("categoryDetail/${category.categoryId}") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = iconVector,
            contentDescription = categoryName,
            modifier = Modifier
                .size(36.dp)  // 아이콘 크기 약간 축소
                .padding(bottom = 2.dp)
        )
        Text(
            text = categoryName,
            fontSize = 12.sp,  // 폰트 크기 약간 축소
            color = Color(0xFF665F5B),
            maxLines = 1,  // 한 줄로 제한
            overflow = TextOverflow.Ellipsis  // 길이가 길면 ...으로 표시
        )
    }
}
