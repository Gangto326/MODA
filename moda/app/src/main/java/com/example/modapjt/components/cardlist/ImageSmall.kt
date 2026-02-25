import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.modapjt.R

@Composable
fun ImageGrid(
    imageUrls: List<String>,
    isMine: Boolean,
    bookMarks: List<Boolean>,
    onClick: (Int) -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = screenWidth / 3

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        imageUrls.chunked(3).forEachIndexed { rowIndex, rowImages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowImages.forEachIndexed { index, imageUrl ->
                    ImageSmall(
                        imageUrl = imageUrl,
                        isMine = isMine,
                        bookMark = bookMarks[rowIndex * 3 + index],
                        onClick = { onClick(rowIndex * 3 + index) },
                        modifier = Modifier.weight(1f)
                    )
                }

                repeat(3 - rowImages.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ImageSmall(
    imageUrl: String,
    modifier: Modifier = Modifier,
    isMine: Boolean,
    bookMark: Boolean,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = screenWidth / 3

    Box(
        modifier = modifier
            .size(imageSize)
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(if (!isMine) MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.tertiary)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 남의 글(isMine=false)인 경우에만 오른쪽 아래에 아이콘 표시
        if (!isMine) {
            Image(
                painter = painterResource(id = R.drawable.ic_other_people),
                contentDescription = "Other's content",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(20.dp)
            )
        }
    }
}