
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val REQUEST_PERMISSION_CODE = 100

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageDetailScreen(cardDetail: CardDetail, navController: NavController) {
    var showPermissionDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp) // 옆 패딩만 주는걸로 변경
    ) {
        item {
            // 이미지 표시
            cardDetail.thumbnailUrl?.let { imageUrl ->
                ZoomableImage(
                    imageUrl = imageUrl,
                    imageOriginalWidth = 300f,
                    imageOriginalHeight =  500f,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.onSecondary, thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))


            // 카테고리 & 날짜 추가
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (cardDetail.categoryId) {
                        1 -> "전체"
                        2 -> "트렌드"
                        3 -> "오락"
                        4 -> "금융"
                        5 -> "여행"
                        6 -> "음식"
                        7 -> "IT"
                        8 -> "디자인"
                        9 -> "사회"
                        10 -> "건강"
                        else -> "기타"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 키워드 및 이미지 저장 버튼 추가
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    maxItemsInEachRow = 3
                ) {
//                    cardDetail.keywords.take(3).forEach { keyword ->
//                        Surface(
//                            shape = RoundedCornerShape(20.dp),
//                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
//                            color = Color.Transparent,
//                            modifier = Modifier
//                                .padding(end = 8.dp, bottom = 16.dp)
//                                .clickable(
//                                    indication = null, // 클릭 효과 제거
//                                    interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
//                                ) {
//                                    if (keyword.isNotBlank()) {
//                                        navController.navigate("newSearchCardListScreen/$keyword")
//                                    }
//                                }
//                        ) {
//                            Text(
//                                text = keyword,
//                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Gray
//                            )
//                        }
//                    }
                }

                // ✅ 4️⃣ 아이콘 버튼을 이용한 이미지 저장 버튼
                IconButton(
                    onClick = {
                        scope.launch {
                            if (checkStoragePermission(context)) {
                                saveImageToGallery(context, cardDetail.thumbnailUrl ?: "")
                            } else {
                                showPermissionDialog = true
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "이미지 저장",
                        modifier = Modifier.padding(bottom = 16.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("저장소 권한 필요") },
            text = { Text("갤러리에 이미지를 저장하기 위해 저장소 권한이 필요합니다.") },
            confirmButton = {
                TextButton(onClick = {
                    requestStoragePermission(context)
                    showPermissionDialog = false
                }) {
                    Text("권한 요청")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

// 권한 체크 함수
private fun checkStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

// 권한 요청 함수
private fun requestStoragePermission(context: Context) {
    if (context is ComponentActivity) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                context.requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION_CODE
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                context.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            }
            else -> {
                context.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            }
        }
    }
}

// 이미지 저장 함수
private suspend fun saveImageToGallery(context: Context, imageUrl: String) {
    withContext(Dispatchers.IO) {
        try {
            val bitmap = withContext(Dispatchers.IO) {
                URL(imageUrl).openStream().use {
                    BitmapFactory.decodeStream(it)
                }
            }

            val filename = "MODAP_${System.currentTimeMillis()}.png"
            var fos: OutputStream? = null

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    context.contentResolver.let { resolver ->
                        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                        fos = uri?.let { resolver.openOutputStream(it) }
                    }
                } else {
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val image = File(imagesDir, filename)
                    fos = FileOutputStream(image)
                }

                fos?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "이미지 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                fos?.close()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "이미지 다운로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}