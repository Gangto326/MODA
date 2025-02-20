package com.example.modapjt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreenView {
                // Lottie 애니메이션이 끝나면 MainActivity로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}

@Composable
fun SplashScreenView(onTimeout: () -> Unit) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("logo_start.json")) // assets 폴더에서 불러오기
    val progress by animateLottieCompositionAsState(composition)

    // 애니메이션이 끝나면 MainActivity로 이동
    LaunchedEffect(progress) {
        if (progress == 1f) { // 애니메이션이 끝나면
            delay(500) // 약간의 딜레이 후
            onTimeout()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // 🔥 여기에 원하는 색상 지정!
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.7f) // 🔥 화면 너비의 60% 크기로 자동 조정
                .offset(y = (-30).dp) // 🔥 Y축 위치 조정 (위로 50dp 이동)
                .aspectRatio(1f) // 🔥 정사각형 비율 유지
                .align(Alignment.Center)        )
    }
}
