
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R

@Composable
fun TypeSelectBar( // 상단 타입 선택 바
    selectedCategory: String,
    selectedSort: String, // 추가: 현재 선택된 정렬
    onCategorySelected: (String) -> Unit,
    onSortSelected: (String) -> Unit, // 정렬 선택 이벤트 추가
    modifier: Modifier = Modifier,
) {
    val categories = listOf("전체", "동영상", "블로그", "뉴스", "이미지")
//    var selectedSort by remember { mutableStateOf("최신순") } // 기본 정렬 상태

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White) // 수정완료
            .padding(horizontal = 16.dp, vertical = 10.dp), // 수정완료( 상단 타입 선택바 세로 길이 )
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 카테고리 탭 (왼쪽 정렬)
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(categories) { category ->
                CategoryTab(
                    title = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(category)
                        onSortSelected("최신순") // 카테고리 변경 시 정렬 초기화
                    }
                )
            }
        }

        // 정렬 드롭다운 버튼 (오른쪽 끝 정렬)
        // "전체" 탭이 아닐 때만 정렬 버튼 표시
        if (selectedCategory != "전체") {
            SortDropdown(
                selectedSort = selectedSort,
                onSortSelected = onSortSelected
//                selectedSort = selectedSort,
//                onSortSelected = { selectedSort = it },
//                modifier = Modifier.align(Alignment.CenterVertically) // 오른쪽 끝에 위치
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
    val iconResId = when (title) {
        "이미지" -> R.drawable.ic_image
        "동영상" -> R.drawable.ic_video
        "블로그" -> R.drawable.ic_blog
        "뉴스" -> R.drawable.ic_news
        else -> null
    }

    var rowWidth by remember { mutableStateOf(0) } // 🔥 Row 전체 너비 저장
    val density = LocalDensity.current // 🔥 DP 변환을 위한 density 객체

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp) // 🔹 선택된 탭의 여백 조정 (필요시 수정)
    ) {
        // 🔹 아이콘 + 텍스트를 감싸는 Row (너비 측정 대상)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    rowWidth = layoutCoordinates.size.width // 🔹 Row의 너비 저장
                }
        ) {
            // 🔹 선택된 경우만 아이콘 표시
            if (isSelected && iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "$title 아이콘",
                    modifier = Modifier
                        .size(20.dp) // 아이콘 크기 조정
                        .padding(end = 6.dp) // 텍스트와 간격

                )
            }

            // 🔹 카테고리 텍스트
            Text(
                text = title,
                color = if (isSelected) Color(0xFF665F5B) else Color(0xFFBAADA4),
                fontSize = 16.sp, // 🔥 폰트 크기 설정 (sp 단위)
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//                fontWeight = FontWeight.Bold // 🔥 폰트 굵기 설정
//                style = customTypography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(6.dp)) // 🔹 텍스트와 밑줄 간격 추가

        // 🔹 밑줄 (아이콘 포함한 Row 기준으로 너비 설정)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(with(density) { rowWidth.toDp() } + 12.dp) // 🔥 아이콘 포함한 Row 길이로 설정 + 12dp
                    .height(2.dp)
                    .background(Color(0xFFFFCD69))
            )
        }
    }
}









@Composable
fun SortDropdown(
    selectedSort: String,
    onSortSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        // 정렬 버튼
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFFFCC80), RoundedCornerShape(16.dp)) // 노란색 테두리 추가
                .background(Color.Transparent)
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedSort,
                color = Color(0xFFBDBDBD),
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "드롭다운",
                tint = Color(0xFFBDBDBD)
            )
        }

        // 드롭다운 메뉴
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            listOf("최신순", "오래된순").forEach { sortOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = sortOption,
                            color = if (selectedSort == sortOption) Color.Black else Color.DarkGray
                        )
                    },
                    modifier = Modifier
                        .background(if (selectedSort == sortOption) Color(0xFFFFCC80) else Color.White),
                    onClick = {
                        onSortSelected(sortOption)
                        expanded = false
                    }
                )
            }
        }
    }
}