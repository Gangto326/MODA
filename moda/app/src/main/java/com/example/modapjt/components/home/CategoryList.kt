//package com.example.modapjt.components.home
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.Image
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.modapjt.domain.viewmodel.CategoryViewModel
//
//@Composable
//fun CategoryList(navController: NavController, viewModel: CategoryViewModel = viewModel()) {
//    val categories by viewModel.categories.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadCategories("user") // 테스트용 userId
//    }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            categories.take(5).forEach { category ->
//                CategoryItem(category = category, navController = navController)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            categories.drop(5).take(5).forEach { category ->
//                CategoryItem(category = category, navController = navController)
//            }
//        }
//    }
//}


package com.example.modapjt.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Category
import com.example.modapjt.domain.viewmodel.CategoryViewModel

// `CategoryItem.kt`에서 이미 정의된 `CategoryItem` 가져오기
import com.example.modapjt.components.home.CategoryItem

@Composable
fun CategoryList(
    navController: NavController,
    viewModel: CategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories("user") // 테스트용 userId
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // position 값이 1~5인 카테고리 (위쪽)
        val topCategories = categories.filter { it.position in 1..5 }

        // position 값이 6~10인 카테고리 (아래쪽)
        val bottomCategories = categories.filter { it.position in 6..10 }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            topCategories.forEach { category ->
                CategoryItem(category = category, navController = navController)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomCategories.forEach { category ->
                CategoryItem(category = category, navController = navController)
            }
        }
    }
}
