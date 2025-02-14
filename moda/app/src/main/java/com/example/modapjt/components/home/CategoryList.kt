package com.example.modapjt.components.home

// `CategoryItem.kt`에서 이미 정의된 `CategoryItem` 가져오기
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.CategoryViewModel

@Composable
fun CategoryList(
    navController: NavController,
    viewModel: CategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories("user")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),  // 전체 여백 증가
        verticalArrangement = Arrangement.spacedBy(16.dp)  // 상하 Row 사이 간격
    ) {
        // 상단 카테고리 Row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly  // 균등 간격
        ) {
            categories
                .filter { it.position in 1..5 }
                .forEach { category ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),  // 좌우 간격
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryItem(category = category, navController = navController)
                    }
                }
        }

        // 하단 카테고리 Row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories
                .filter { it.position in 6..10 }
                .forEach { category ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),  // 좌우 간격
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryItem(category = category, navController = navController)
                    }
                }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))


    Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
    Spacer(modifier = Modifier.height(16.dp))

}