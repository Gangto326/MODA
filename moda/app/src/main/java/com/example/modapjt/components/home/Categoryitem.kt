package com.example.modapjt.components.home

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.domain.model.Category
import kotlinx.coroutines.launch

@Composable
fun CategoryItem(
    category: Category,
    navController: NavController,
    isVisible: Boolean
) {
    var rippleCenter by remember { mutableStateOf(Offset.Zero) }
    var rippleRadius by remember { mutableStateOf(0f) }
    var rippleAlpha by remember { mutableStateOf(0f) }
    val rippleAnimatable = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val categoryIdMap = mapOf(
        1 to "전체",
        2 to "건강",
        3 to "여행",
        4 to "음식",
        5 to "IT",
        6 to "경제",
        7 to "문화",
        8 to "과학",
        9 to "취미",
        10 to "예술"
    )

    val categoryName = categoryIdMap[category.categoryId] ?: category.category

    // 활성화 상태에 따라 다른 아이콘 사용
    val iconVector = if (isVisible) {
        //다크모드라면
        if (isSystemInDarkTheme()) {
            when (category.categoryId) {
                1 -> R.drawable.cg_d_all
                2 -> R.drawable.cg_d_health
                3 -> R.drawable.cg_d_travel
                4 -> R.drawable.cg_d_food
                5 -> R.drawable.cg_d_it
                6 -> R.drawable.cg_d_finance
                7 -> R.drawable.cg_d_society
                8 -> R.drawable.cg_d_science
                9 -> R.drawable.cg_d_hobby
                10 -> R.drawable.cg_d_art
                else -> R.drawable.category_default
            }
            //다크모드가 아니라면
        } else {
            when (category.categoryId) {
                1 -> R.drawable.cg_c_all
                2 -> R.drawable.cg_c_health
                3 -> R.drawable.cg_c_travel
                4 -> R.drawable.cg_c_food
                5 -> R.drawable.cg_c_it
                6 -> R.drawable.cg_c_finance
                7 -> R.drawable.cg_c_society
                8 -> R.drawable.cg_c_science
                9 -> R.drawable.cg_c_hobby
                10 -> R.drawable.cg_c_art
                else -> R.drawable.category_default
            }
        }
    } else {
        // 비활성화된 경우 cg_ 접두사 아이콘 사용
        when (category.categoryId) {
            1 -> R.drawable.cg_all
            2 -> R.drawable.cg_health
            3 -> R.drawable.cg_travel
            4 -> R.drawable.cg_food
            5 -> R.drawable.cg_it
            6 -> R.drawable.cg_finance
            7 -> R.drawable.cg_society
            8 -> R.drawable.cg_science
            9 -> R.drawable.cg_hobby
            10 -> R.drawable.cg_art
            else -> R.drawable.category_default
        }
    }



    // 비활성화 때 사용할 색상 정의
    val inactiveColor = MaterialTheme.colorScheme.onSecondary
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(4.dp)
            .graphicsLayer { alpha = 0.99f }
            .drawBehind {
                drawCircle(
                    color = Color.Gray.copy(alpha = rippleAlpha),
                    radius = rippleRadius,
                    center = rippleCenter
                )
            }
            .then (
                if (isVisible) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { offset ->
                            rippleCenter = offset
                            scope.launch {
                                rippleAnimatable.snapTo(0f)
                                rippleAnimatable.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(300)
                                ) {
                                    rippleRadius = this.value * maxOf(size.width, size.height) * 1.2f
                                    rippleAlpha = (1f - this.value) * 0.15f
                                }
                            }
                            navController.navigate("categoryDetail/${category.categoryId}")
                        }
                    }
                } else {
                    Modifier.clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){
                        Toast.makeText(
                            context,
                            "$categoryName 컨텐츠를 채워 활성화해주세요.",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconVector),
            contentDescription = categoryName,
            // 활성화 상태에 따라 아이콘이 이미 다르므로 colorFilter는 필요 없음
            modifier = Modifier
                .size(36.dp)
                .padding(bottom = 2.dp)
        )

        val textColor = when {
            !isVisible -> inactiveColor
            isSystemInDarkTheme() -> Color.White
            else -> Color.LightGray
        }


        Text(
            text = categoryName,
            fontSize = 12.sp,
            color = textColor,  // 여기 적용
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}