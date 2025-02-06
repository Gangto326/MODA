import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun TypeSelectBar(
//    modifier: Modifier = Modifier,
//    onCategorySelected: (String) -> Unit = {}
//) {
//    // 카테고리 목록
//    val categories = listOf("전체", "이미지", "동영상", "블로그", "뉴스")
//
//    // 선택된 카테고리 상태 관리
//    var selectedCategory by remember { mutableStateOf(categories[0]) }
//
//    LazyRow(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(categories) { category ->
//            CategoryTab(
//                title = category,
//                isSelected = selectedCategory == category,
//                onClick = {
//                    selectedCategory = category
//                    onCategorySelected(category)
//                }
//            )
//        }
//    }
//}



@Composable
fun TypeSelectBar(
    selectedCategory: String, // ✅ 선택된 카테고리를 외부에서 전달받음
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("전체", "이미지", "동영상", "블로그", "뉴스")

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryTab(
                title = category,
                isSelected = selectedCategory == category, // ✅ 외부에서 전달받은 상태 사용
                onClick = { onCategorySelected(category) } // ✅ 선택 이벤트 외부로 전달
            )
        }
    }
}




@Composable
private fun CategoryTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Color(0xFF4A90E2)  // 선택된 탭 파란색 배경
                else Color(0xFFF5F5F5)  // 선택되지 않은 탭 회색 배경
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

//// 사용 예시
//@Composable
//fun PreviewCategoryTypeComponent() {
//    CategoryTypeComponent(
//        onCategorySelected = { category ->
//            // 선택된 카테고리 처리
//            println("Selected category: $category")
//        }
//    )
//}