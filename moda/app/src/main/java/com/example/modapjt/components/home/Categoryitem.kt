package com.example.modapjt.components.home

import android.graphics.drawable.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Category

@Composable
fun CategoryItem(category: Category, navController: NavController) {
    val iconVector = when (category.category) {
//            val iconResId = when (category.category) {
//        "All" -> R.drawable.ic_all
//        "Trends" -> R.drawable.ic_trends
//        "Entertainment" -> R.drawable.ic_entertainment
//        "Finance" -> R.drawable.ic_finance
//        "Travel" -> R.drawable.ic_travel
//        "Food" -> R.drawable.ic_food
//        "IT" -> R.drawable.ic_it
//        "Design" -> R.drawable.ic_design
//        "Society" -> R.drawable.ic_society
//        "Health" -> R.drawable.ic_health
//        else -> R.drawable.ic_default

        "All" -> Icons.Default.AccountBox
        "Trends" -> Icons.Default.AccountBox
        "Entertainment" -> Icons.Default.AccountBox
        "Finance" -> Icons.Default.AccountBox
        "Travel" -> Icons.Default.AccountBox
        "Food" -> Icons.Default.Add
        "IT" -> Icons.Default.Add
        "Design" -> Icons.Default.Add
        "Society" ->Icons.Default.Add
        "Health" -> Icons.Default.Add
        else -> Icons.Default.Done
    }

    Column(
        modifier = Modifier
            .width(80.dp)  // 명시적 너비 설정
            .padding(8.dp)
            .clickable {  navController.navigate("categoryDetail/${category.category}") },
        horizontalAlignment = Alignment.CenterHorizontally  // 가운데 정렬 추가
    ) {
        Image(
            imageVector = iconVector,
            contentDescription = category.category,
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 4.dp)  // 아이콘과 텍스트 사이 간격
        )
        Text(
            text = category.category,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}