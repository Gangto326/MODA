//package com.example.modapjt.components.user
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import com.example.modapjt.R
//
//@Composable
//fun UserProfileCard(
//    profileImage: String?, // ✅ null 가능
//    nickname: String, // ✅ 사용자 닉네임
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {} // 클릭 시 실행할 동작
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable(onClick = onClick), // 클릭 이벤트 추가
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween // ✅ 오른쪽 정렬 적용
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            // ✅ 프로필 이미지 (동그랗게 유지)
//            AsyncImage(
//                model = profileImage ?: "https://example.com/default_profile.png", // 기본 이미지 URL
//                contentDescription = "Profile Image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(64.dp) // 크기 증가
//                    .clip(CircleShape) // 동그랗게 유지
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = nickname, // ✅ 닉네임
//                    fontSize = 20.sp, // ✅ 닉네임 크기 증가
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    modifier = Modifier.alignByBaseline() // ✅ 기준선을 맞추도록 추가
//                )
//                Spacer(modifier = Modifier.width(3.dp))
//                Text(
//                    text = "님 안녕하세요", // ✅ 인사말
//                    fontSize = 15.sp, // ✅ 크기 조정
//                    fontWeight = FontWeight.Normal,
//                    color = Color.Gray,
//                    modifier = Modifier.alignByBaseline() // ✅ 기준선을 맞추도록 추가
//                )
//            }
//
//        }
//
//
//        // ✅ 오른쪽 아이콘 크기 키우고 정렬
//        Icon(
//            painter = painterResource(id = R.drawable.ic_arrow_right),
//            contentDescription = "Navigate",
//            modifier = Modifier
//                .size(15.dp)
//                .clickable { /* TODO: 이동 클릭 이벤트 */ }
//        )
//    }
//}

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R

@Composable
fun UserProfileCard(nickname: String, itemCount: Int) {
    // 저장 개수에 따라 PNG 이미지 및 상태 텍스트 설정
    val (imageRes, statusText) = when {
        itemCount < 10 -> Pair(R.drawable.moda_logo, "수집가1")
        itemCount in 10..49 -> Pair(R.drawable.moda_logo, "수집가2")
        itemCount in 50..99 -> Pair(R.drawable.moda_logo, "프로 수집러")
        else -> Pair(R.drawable.moda_logo, "마스터 수집러")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 회색 원 대신 PNG 이미지 배치
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "수집 상태 아이콘",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 닉네임 및 상태 텍스트 정렬 (첨부된 이미지와 동일한 배치)
        Column {
            Text(
                text = statusText,  // 별명 (수집 상태)
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp, // ✅ 닉네임과 동일한 크기로 설정
                    fontWeight = FontWeight.Normal
                ),
                color = Color.Gray
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = nickname,  // 실제 닉네임
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "님 안녕하세요",  // ✅ 닉네임과 같은 크기 및 정렬
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Gray
                )
            }
        }
    }
}

