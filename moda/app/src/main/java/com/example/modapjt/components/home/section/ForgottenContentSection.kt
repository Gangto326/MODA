package com.example.modapjt.components.home.section

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.BottomThumbnail
import com.example.modapjt.components.home.ForgottenContentItem
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.data.dto.response.SearchItem
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun ForgottenContentSection(
    forgottenContents: List<SearchItem>,
    navController: NavController
) {
    if (forgottenContents.isNotEmpty()) {
        Divider(
            color = MaterialTheme.colorScheme.onTertiary,
            thickness = 6.dp,
            modifier = Modifier.padding(horizontal = 0.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        HomeSmallTitle(
            title = "잊고있던 컨텐츠",
            description = "| 추억이 담긴 나의 컨텐츠"
        )

        // ForgottenContentItem 컴포넌트의 내용을 직접 구현
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (item in forgottenContents) {
                    BottomThumbnail(
                        cardId = item.cardId,
                        thumbnailUrl = item.thumbnailUrl ?: "",
                        title = item.title ?: "",
                        type = item.type ?: "",
                        keywords = item.keywords ?: emptyList(),
                        bookmark = item.bookmark ?: false,
                        onClick = { cardId ->
                            navController.navigate("cardDetail/${item.cardId}")
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
