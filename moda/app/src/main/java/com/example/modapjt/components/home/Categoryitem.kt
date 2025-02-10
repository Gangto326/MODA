package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        "Trends" -> Icons.Default.AccountBox
        "Entertainment" -> Icons.Default.AccountBox
        "Finance" -> Icons.Default.AccountBox
        "Travel" -> Icons.Default.AccountBox
        "Food" -> Icons.Default.Add
        "IT" -> Icons.Default.Add
        "Design" -> Icons.Default.Add
        "Society" -> Icons.Default.Add
        "Health" -> Icons.Default.Add
        else -> Icons.Default.Done
    }

    val categoryName = categoryNameMap[category.category] ?: category.category

    Column(
        modifier = Modifier
            .width(80.dp)
            .padding(8.dp)
            .clickable { navController.navigate("categoryDetail/${category.categoryId}") }, // categoryId 전달
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = iconVector,
            contentDescription = categoryName,
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 4.dp)
        )
        Text(
            text = categoryName,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}
