package com.example.modapjt.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Category
import com.example.modapjt.domain.viewmodel.CategoryViewModel

@Composable
fun CategoryList(navController: NavController,
                 viewModel: CategoryViewModel = viewModel(),
                 onCategorySelected: (String) -> Unit // 카테고리 선택 시 실행될 콜백 추가
                ) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories("user") // 테스트용 userId
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.take(5).forEach { category ->
                CategoryItem(category = category, navController = navController)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.drop(5).take(5).forEach { category ->
                CategoryItem(category = category, navController = navController)
            }
        }
    }
}


@Composable
fun CategoryItem(
    category: Category,
    onCategorySelected: (String) -> Unit // 선택된 카테고리를 전달하는 콜백
) {
    Column(
        modifier = Modifier
            .clickable { onCategorySelected(category.category) } // 클릭하면 선택된 카테고리를 전달
            .padding(8.dp)
    ) {
        Text(
            text = category.category, // 카테고리 이름만 표시
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}