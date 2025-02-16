@file:OptIn(ExperimentalLayoutApi::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.draw.clip



@Composable
fun ImageBig(
    imageUrl: String,
    isMine: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.FillWidth, // 가로 너비에 맞추고 높이는 비율 유지
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // 둥근 모서리 적용
            .clickable(onClick = onClick)
            .then(
                if (!isMine) Modifier.border(2.dp, Color.Red, RoundedCornerShape(12.dp)) else Modifier // isMine이 false면 빨간 테두리
            )
    )
}


@Composable
fun ImageGrid(imageUrls: List<String>, isMine: Boolean) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp), // 최소 100dp 유지하며 가변형 정렬
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), // 좌우 패딩 추가
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 열 간격 추가
        verticalArrangement = Arrangement.spacedBy(8.dp) // 행 간격 추가
    ) {
        items(imageUrls.size) { index ->
            ImageBig(
                imageUrl = imageUrls[index],
                isMine = isMine
            )
        }
    }
}
