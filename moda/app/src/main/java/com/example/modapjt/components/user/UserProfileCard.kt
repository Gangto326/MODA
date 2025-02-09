package com.example.modapjt.components.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.modapjt.R

@Composable
fun UserProfileCard(
    profileImage: String?, // ✅ null 가능
    nickname: String, // ✅ 사용자 닉네임
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // 클릭 시 실행할 동작
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick), // 클릭 이벤트 추가
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // ✅ 오른쪽 정렬 적용
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // ✅ 프로필 이미지 (동그랗게 유지)
            AsyncImage(
                model = profileImage ?: "https://example.com/default_profile.png", // 기본 이미지 URL
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp) // 크기 증가
                    .clip(CircleShape) // 동그랗게 유지
            )

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nickname, // ✅ 닉네임
                    fontSize = 20.sp, // ✅ 닉네임 크기 증가
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.alignByBaseline() // ✅ 기준선을 맞추도록 추가
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "님 안녕하세요", // ✅ 인사말
                    fontSize = 15.sp, // ✅ 크기 조정
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    modifier = Modifier.alignByBaseline() // ✅ 기준선을 맞추도록 추가
                )
            }

        }


        // ✅ 오른쪽 아이콘 크기 키우고 정렬
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Navigate",
            modifier = Modifier
                .size(15.dp)
                .clickable { /* TODO: 이동 클릭 이벤트 */ }
        )
    }
}
