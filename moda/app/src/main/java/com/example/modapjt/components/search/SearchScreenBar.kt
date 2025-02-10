package com.example.modapjt.components.search

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SearchScreenBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    initialValue: String = "",
    isSearchActive: Boolean,
    onSearchValueChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onBackPressed: () -> Unit,
    context: Context // 🔹 Context 추가
) {
    var searchText by remember { mutableStateOf(initialValue) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // ✅ 화면이 열리자마자 키보드 활성화
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { onBackPressed() }, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                contentAlignment = Alignment.CenterStart // ✅ 내용 가운데 정렬
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "찾고 싶은 내용을 입력하세요",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.sp
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearchValueChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState -> onFocusChanged(focusState.isFocused) }
                        .padding(vertical = 8.dp), // ✅ 위아래 패딩 추가
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp,
                        lineHeight = 24.sp, // ✅ 줄 높이 설정 (커서 중앙 정렬)
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }


            IconButton(
                onClick = {
                    if (searchText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                            val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                        }
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp).padding(start = 8.dp)
                )
            }
        }
    }
}
