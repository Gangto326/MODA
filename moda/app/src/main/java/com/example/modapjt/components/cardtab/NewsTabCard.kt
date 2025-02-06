import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



@Composable
fun NewsabCard(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) { // LazyColumn 대신 Column 사용
        repeat(30) { index ->
            ImageBig(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        println("Image $index 클릭됨")
                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
                    }
            )
        }
    }
}