package com.example.modapjt.components.home.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.ForgottenContentItem
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun ForgottenContentSection(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val searchData by searchViewModel.searchData.collectAsState()
    val forgottenContents = searchData?.forgotten.orEmpty() // 잊고 있던 컨텐츠 리스트 가져오기

    Divider(color = Color(0xFFF1F1F1), thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))
    Spacer(modifier = Modifier.height(20.dp))

    if (forgottenContents.isNotEmpty()) {
        HomeSmallTitle(
            title = "잊고있던 컨텐츠",
            description = "| 해당 컨텐츠들에 대한 설명"
        )

        ForgottenContentItem(navController, searchViewModel)
        Spacer(modifier = Modifier.height(20.dp))
        
    }
}
