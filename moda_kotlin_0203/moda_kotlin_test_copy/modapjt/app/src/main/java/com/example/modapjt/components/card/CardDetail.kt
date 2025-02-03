package com.example.modapjt.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modapjt.domain.model.Card

@Composable
fun CardDetail(card: Card) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "제목: ${card.title}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "내용: ${card.content}")

        if (card.isView) { // 임베딩된 콘텐츠가 있을 경우
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "임베딩된 콘텐츠:")
            // 여기에서 임베딩된 콘텐츠 유형에 맞는 뷰 추가
            // ex) 이미지나 비디오를 추가하려면 조건에 맞게 구현 가능
            // Text(text = card.embedContentText) // 예시로 텍스트 콘텐츠
        }
    }
}
