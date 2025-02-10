//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//
//@Composable
//fun TypeSelectBar(
//    selectedCategory: String, // 선택된 카테고리를 외부에서 전달받음
//    modifier: Modifier = Modifier,
//    onCategorySelected: (String) -> Unit
//) {
//    val categories = listOf("전체", "이미지", "동영상", "블로그", "뉴스")
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
//                isSelected = selectedCategory == category, // 외부에서 전달받은 상태 사용
//                onClick = { onCategorySelected(category) } // 선택 이벤트 외부로 전달
//            )
//        }
//    }
//}
//
//
//
//
//@Composable
//private fun CategoryTab(
//    title: String,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(20.dp))
//            .background(
//                if (isSelected) Color(0xFF4A90E2)  // 선택된 탭 파란색 배경
//                else Color(0xFFF5F5F5)  // 선택되지 않은 탭 회색 배경
//            )
//            .clickable(onClick = onClick)
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = title,
//            color = if (isSelected) Color.White else Color.Black,
//            fontSize = 14.sp,
//            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//        )
//    }
//}

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypeSelectBar(
    selectedCategory: String,
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("전체", "이미지", "동영상", "블로그", "뉴스")
    var selectedSort by remember { mutableStateOf("최신순") } // 기본 정렬 상태

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 카테고리 탭 (왼쪽 정렬)
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryTab(
                    title = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        selectedSort = "최신순"  // 탭 변경 시 정렬 초기화
                        onCategorySelected(category)
                    }
                )
            }
        }

        // 정렬 드롭다운 버튼 (오른쪽 끝 정렬)
        // "전체" 탭이 아닐 때만 정렬 버튼 표시
        if (selectedCategory != "전체") {
            SortDropdown(
                selectedSort = selectedSort,
                onSortSelected = { selectedSort = it },
                modifier = Modifier.align(Alignment.CenterVertically) // 오른쪽 끝에 위치
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.Black else Color(0xFFBDBDBD), // 선택된 경우 검정, 아닐 경우 회색 (#BDBDBD)
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .background(Color(0xFFFFCC80)) // 밑줄 색상: 주황색 (#FFCC80)
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