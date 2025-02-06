import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//@Composable
//fun VideoTabCard(
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .size(100.dp)
//            .clip(RoundedCornerShape(8.dp))
//            .background(Color(0xFFE0E0E0))  // 회색 배경
//    )
//}


@Composable
fun VideoTabCard(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE0E0E0))  // 회색 배경
    )
}