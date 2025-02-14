package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R


@Composable
fun BottomThumbnail(
    cardId: String,
    thumbnailUrl: String,
    title: String,
    type: String,
    keywords: List<String>,
    bookmark: Boolean,
    onClick: (String) -> Unit
) {

    android.util.Log.d("BottomThumbnail", "Bookmark State: $bookmark")

    Column(
        modifier = Modifier
            .width(150.dp) // 썸네일 크기 조정
            .clickable { onClick(cardId) }
    ) {
        // 썸네일 이미지 + 북마크 아이콘
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        ) {
            coil.compose.AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Thumbnail Image",
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // 북마크 아이콘
            Icon(
                painter = painterResource(
                    if (bookmark==true) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
                ),
                contentDescription = "Bookmark Icon",
                tint = if (bookmark) Color(0xFFFFA500) else Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
            )
        }

        // 제목 (두 줄 초과하면 '...' 처리)
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        // 뉴스 아이콘 + 키워드 (최대 3개)
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: Painter = painterResource(
                when (type) {
                    "NEWS" -> R.drawable.ic_news
                    "VIDEO" -> R.drawable.ic_youtube
                    "BLOG" -> R.drawable.ic_blog
                    else -> R.drawable.ic_default
                }
            )

            Icon(
                painter = icon,
                contentDescription = "Type Icon",
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "${getTypeText(type)} | ${keywords.take(3).joinToString(" ")}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}


// type을 한글로 변환하는 함수
fun getTypeText(type: String): String {
    return when (type) {
        "NEWS" -> "뉴스"
        "VIDEO" -> "유튜브"
        "BLOG" -> "블로그"
        else -> "기타"
    }
}
