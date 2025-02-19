package com.example.modapjt.components.search

// 기존 imports에 추가
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// NoRippleInteractionSource 구현 (파일 상단에 추가)
private object NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

@Composable
fun SearchScreenBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    initialValue: String = "",
    isSearchActive: Boolean,
    onSearchValueChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onBackPressed: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    context: Context
) {
    var searchText by remember { mutableStateOf(initialValue) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 테두리가 있는 검색창
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .heightIn(min = 48.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // 뒤로가기 버튼 (테두리 안에 위치)
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 4.dp),
                    interactionSource = remember { NoRippleInteractionSource }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp), // 뒤로가기 버튼 공간 확보
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "찾고 싶은 내용을 입력하세요",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.sp
                                )
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
                                .padding(vertical = 8.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.sp,
                                lineHeight = 24.sp,
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchText.isNotBlank()) {
                                        onSearchSubmit(searchText)
                                        keyboardController?.hide()
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                            val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                        }
                                    }
                                }
                            )
                        )
                    }

                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                onSearchValueChange("")
                            },
                            modifier = Modifier.size(48.dp),
                            interactionSource = remember { NoRippleInteractionSource }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            if (searchText.isNotBlank()) {
                                onSearchSubmit(searchText)
                                keyboardController?.hide()
                                CoroutineScope(Dispatchers.IO).launch {
                                    val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                    val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                    SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        interactionSource = remember { NoRippleInteractionSource }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// SearchListBar.kt
@Composable
fun SearchListBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf(query) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 테두리가 있는 검색창
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .heightIn(min = 48.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // 뒤로가기 버튼 (테두리 안에 위치)
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 4.dp),
                    interactionSource = remember { NoRippleInteractionSource }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp), // 뒤로가기 버튼 공간 확보
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "찾고 싶은 내용을 입력하세요",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.sp
                                )
                            )
                        }

                        BasicTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                onQueryChange(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.sp,
                                lineHeight = 24.sp,
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchText.isNotBlank()) {
                                        onSearch(searchText)
                                        keyboardController?.hide()
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                            val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                        }
                                    }
                                }
                            )
                        )
                    }

                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                onQueryChange("")
                            },
                            modifier = Modifier.size(48.dp),
                            interactionSource = remember { NoRippleInteractionSource }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            if (searchText.isNotBlank()) {
                                onSearch(searchText)
                                keyboardController?.hide()
                                CoroutineScope(Dispatchers.IO).launch {
                                    val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                    val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                    SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        interactionSource = remember { NoRippleInteractionSource }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}