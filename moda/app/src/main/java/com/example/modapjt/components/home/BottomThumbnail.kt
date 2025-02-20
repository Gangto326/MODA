package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
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
            .width(224.dp) // 썸네일 크기 조정
            .clickable(
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
            ) { onClick(cardId) }
    ) {
        // 썸네일 이미지 + 북마크 아이콘
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSecondary, shape = RoundedCornerShape(8.dp))
        ) {
            coil.compose.AsyncImage(
                contentDescription = "Thumbnail Image",
                model = if(type=="VIDEO"){
                   "https://img.youtube.com/vi/$thumbnailUrl/0.jpg"
                }else{
                   thumbnailUrl
                },

                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )


            // 북마크 아이콘
            Icon(
                painter = painterResource(
                    if (bookmark==true) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
                ),
                contentDescription = "Bookmark Icon",
                tint = if (bookmark==true) Color.Unspecified else Color.Transparent,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
            )
        }

        // 제목 (두 줄 초과하면 '...' 처리)
        Text(
            text = title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            maxLines = 2,  // ✅ 최대 2줄 표시
            overflow = TextOverflow.Ellipsis,  // ✅ 2줄 초과 시 ... 처리
            lineHeight = 20.sp,  // ✅ 줄 간격 조정
            softWrap = true,  // ✅ 자동 줄 바꿈 허용
            modifier = Modifier
                .padding(top = 10.dp)
                .heightIn(min = 40.dp, max = 48.dp) // ✅ 최소 2줄 높이 보장
        )


// 뉴스 아이콘 + 키워드 (항상 같은 위치)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: Painter = painterResource(
                when (type) {
                    "NEWS" -> R.drawable.ic_a_news
                    "VIDEO" -> R.drawable.ic_a_youtube
                    "BLOG" -> R.drawable.ic_a_blog
                    else -> R.drawable.ic_default
                }
            )

            Icon(
                painter = icon,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Type Icon",
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "${getTypeText(type)} | ${keywords.take(3).joinToString(" ")}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
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
