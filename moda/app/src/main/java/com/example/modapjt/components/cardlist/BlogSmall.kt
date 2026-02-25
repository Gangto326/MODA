import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import com.example.modapjt.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogSmall(
    title: String,
    description: String,
    imageUrl: String,
    isMine: Boolean,
    keywords: List<String>,
    bookMark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    style = customTypography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = customTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // 키워드와 아이콘 영역
        Box(modifier = Modifier.fillMaxWidth()) {
            // 키워드 FlowRow
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                keywords.take(3).forEach { keyword ->
                    Text(
                        text = "# $keyword",
                        style = customTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }

                // 남의 글(isMine=false)인 경우, 빈 공간 추가 (아이콘 공간 확보)
                if (!isMine) {
                    Spacer(modifier = Modifier.width(30.dp))
                }
            }

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
}