package com.example.modapjt.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Card

@Composable
fun CardItem(card: Card, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("card/${card.cardId}") } // 클릭 시 카드 상세 페이지 이동
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = card.thumbnailUrl ?: "썸네일 없음",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = card.thumbnailContent ?: "내용 없음",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}





//
//@Composable
//fun CardItem(card: Card, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { navController.navigate("card/${card.cardId}") }
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "제목: ${card.title}")  // 카드 제목 출력
//            Text(text = "내용: ${card.content}")  // 카드 내용 출력
////            Text(
////                text = card.title,
////                style = MaterialTheme.typography.titleMedium,
////                fontWeight = FontWeight.Bold
////            )
//
//            if (card.isView) { // isView가 true일 때 임베딩된 콘텐츠 표시
//                Spacer(modifier = Modifier.height(8.dp))
////                // 임베딩된 내용 예시 (이미지, 텍스트 등)
////                if (card.embedType == "image") {
////                    Image(
////                        painter = painterResource(id = card.embedContentId),
////                        contentDescription = null,
////                        modifier = Modifier.fillMaxWidth(),
////                        contentScale = ContentScale.Crop
////                    )
////                } else if (card.embedType == "video") {
////                    // 비디오 임베딩 예시
////                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Gray)) {
////                        // 여기 비디오 플레이어 임베딩을 구현할 수 있습니다.
////                    }
////                } else {
////                    // 텍스트 콘텐츠 임베딩
////                    Text(
////                        text = card.embedContentText,
////                        style = MaterialTheme.typography.bodyMedium
////                    )
////                }
//
//                Text(
//                    text = "isView가 true인 경우",
//                )
//            }
//        }
//    }
//}
//




//@Composable
//fun CardItem(card: Card, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { navController.navigate("card/${card.cardId}") } // 클릭 시 상세 페이지 이동
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 카드 제목
//            Text(
//                text = "제목 :: ${card.title}",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            // 카드 내용
//            Text(
//                text = "내용: ${card.content}",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//
//            // isView가 true일 때 콘텐츠 표시 (유튜브 or 블로그)
//            if (card.isView) {
//                Spacer(modifier = Modifier.height(8.dp))
//
//                when (card.typeId) {
//                    1 -> { // typeId == 1 (유튜브)
//                        val videoId = extractYouTubeVideoId(card.urlHash)
//                        if (!videoId.isNullOrEmpty()) {
//                            YouTubePlayer(videoId = videoId, modifier = Modifier.height(200.dp))
//                        }
//                    }
//                    2 -> { // typeId == 2 (블로그)
//                        Text(
//                            text = "아직 테스트",
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier
//                                .background(Color.LightGray)
//                                .padding(8.dp)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
//}