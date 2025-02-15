//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import com.example.modapjt.domain.model.CardDetail
//
///**
// * 이미지 중심의 컨텐츠를 보여주는 상세 화면 컴포저블
// * 이미지와 관련 메타 정보를 세로로 배치
// *
// * @param cardDetail 표시할 이미지 컨텐츠의 상세 정보를 담은 객체
// */
//@Composable
//fun ImageDetailScreen(cardDetail: CardDetail) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        // 썸네일 이미지가 있는 경우에만 표시
//        cardDetail.thumbnailUrl?.let {
//            Image(
//                painter = rememberAsyncImagePainter(it), // 비동기적으로 이미지 로드
//                contentDescription = "이미지",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//            )
//        }
//
//        // 이미지 관련 키워드
//        Text(text = "키워드: ${cardDetail.keywords.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)
//
//        // 이미지 생성/업로드 날짜
//        Text(text = "생성 날짜: ${cardDetail.createdAt}", style = MaterialTheme.typography.bodySmall)
//    }
//}



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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageDetailScreen(cardDetail: CardDetail) {
    val searchViewModel: SearchViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showPermissionDialog by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            cardDetail.thumbnailUrl?.let { imageUrl ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "이미지",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Button(
                        onClick = {
                            if (checkStoragePermission(context)) {
                                scope.launch {
                                    saveImageToGallery(context, imageUrl)
                                }
                            } else {
                                showPermissionDialog = true
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text("이미지 저장")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val limitedKeywords = cardDetail.keywords
            Text(
                text = "키워드: ${limitedKeywords.take(3).joinToString(", ")}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            cardDetail.content?.let { content ->
                MarkdownText(
                    markdown = content,
                    modifier = Modifier.padding(vertical = 8.dp),
                    keywords = limitedKeywords,
                    onKeywordClick = { keyword ->
                        searchViewModel.onKeywordClick(keyword)
                    }
                )
            }

            Text(
                text = "생성 날짜: $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
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
                    Toast.makeText(context, "이미지 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                fos?.close()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "이미지 다운로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}