package com.example.modapjt.components.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.domain.model.Category


@Composable
fun CategoryItem(
    category: Category,
    navController: NavController,
//    isVisible: Boolean
) {
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
        "All" -> R.drawable.category_all
        "Trends" -> R.drawable.category_trends
        "Entertainment" -> R.drawable.category_entertainment
        "Finance" -> R.drawable.category_finance
        "Travel" -> R.drawable.category_travel
        "Food" -> R.drawable.category_food
        "IT" -> R.drawable.category_it
        "Design" -> R.drawable.category_design
        "Society" -> R.drawable.category_society
        "Health" -> R.drawable.category_health
        else -> R.drawable.category_default
    }

    val categoryName = categoryNameMap[category.category] ?: category.category
//    val color = if (isVisible) Color(0xFF665F5B) else Color(0xFFC1C1C1)
//    val context = LocalContext.current

//    Column(
//        modifier = Modifier
//            .padding(4.dp)  // 패딩 값 축소
//            .then (
//                if (isVisible) {
//                    Modifier.clickable { navController.navigate("categoryDetail/${category.categoryId}") }
//                } else {
//                    Modifier.clickable {
//                        Toast.makeText(
//                            context,
//                            "$categoryName 컨텐츠를 채워 활성화해주세요.",
//                            Toast.LENGTH_SHORT)
//                        .show()
//                    }
//                }
//            ),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = iconVector),
//            contentDescription = categoryName,
//            colorFilter = ColorFilter.tint(color),
//            modifier = Modifier
//                .size(36.dp)  // 아이콘 크기 약간 축소
//                .padding(bottom = 2.dp)
//        )
//        Text(
//            text = categoryName,
//            fontSize = 12.sp,  // 폰트 크기 약간 축소
//            color = color,
//            maxLines = 1,  // 한 줄로 제한
//            overflow = TextOverflow.Ellipsis  // 길이가 길면 ...으로 표시
//        )
//    }
    // -> 이건 기존 코드로 해야하는데 잠시 주석처리
    Column(
        modifier = Modifier
            .padding(4.dp)  // 패딩 값 축소
            .clickable { navController.navigate("categoryDetail/${category.categoryId}") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconVector),
            contentDescription = categoryName,
//            colorFilter = ColorFilter.tint(color),  // 잠시 주석처리
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
