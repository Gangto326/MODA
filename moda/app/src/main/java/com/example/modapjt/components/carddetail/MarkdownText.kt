//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import org.commonmark.node.BulletList
//import org.commonmark.node.Code
//import org.commonmark.node.Emphasis
//import org.commonmark.node.FencedCodeBlock
//import org.commonmark.node.Heading
//import org.commonmark.node.Link
//import org.commonmark.node.ListItem
//import org.commonmark.node.Node
//import org.commonmark.node.Paragraph
//import org.commonmark.node.StrongEmphasis
//import org.commonmark.node.Text
//import org.commonmark.parser.Parser
//
//@Composable
//fun MarkdownText(
//    markdown: String,
//    modifier: Modifier = Modifier,
//    color: Color = MaterialTheme.colorScheme.onSurface
//) {
//    val parser = remember { Parser.builder().build() }
//    val document = remember(markdown) { parser.parse(markdown) }
//    val annotatedString = remember(document) { document.toAnnotatedString() }
//
//    Column(modifier = modifier) {
//        Text(
//            text = annotatedString,
//            color = color,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}
//
//private fun Node.toAnnotatedString(): AnnotatedString {
//    return buildAnnotatedString {
//        processNode(this@toAnnotatedString)
//    }
//}
//
//@Composable
//private fun CodeBlock(content: String, modifier: Modifier = Modifier) {
//    Text(
//        text = content,
//        modifier = modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(4.dp))
//            .background(MaterialTheme.colorScheme.surfaceVariant)
//            .padding(8.dp),
//        fontFamily = FontFamily.Monospace,
//        style = MaterialTheme.typography.bodyMedium
//    )
//}
//
//private fun AnnotatedString.Builder.processNode(node: Node, listLevel: Int = 0) {
//    when (node) {
//        is Heading -> {
//            append("\n")
//            pushStyle(
//                SpanStyle(
//                    fontWeight = FontWeight.Bold,
//                    fontSize = when (node.level) {
//                        1 -> 24.sp
//                        2 -> 20.sp
//                        3 -> 18.sp
//                        else -> 16.sp
//                    }
//                )
//            )
//            node.firstChild?.let { processNode(it) }
//            pop()
//            append("\n")
//        }
//        is BulletList -> {
//            node.firstChild?.let {
//                processNode(it, listLevel + 1)
//            }
//        }
//        is ListItem -> {
//            append("\n")
//            append("  ".repeat(listLevel))
//            append("• ")
//            node.firstChild?.let { processNode(it, listLevel) }
//            node.next?.let { processNode(it, listLevel) }
//        }
//        is Paragraph -> {
//            if (node.parent !is ListItem) append("\n")
//            node.firstChild?.let { processNode(it) }
//            if (node.parent !is ListItem) append("\n")
//        }
//        is Text -> {
//            append(node.literal)
//        }
//        is StrongEmphasis -> {
//            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
//            node.firstChild?.let { processNode(it) }
//            pop()
//        }
//        is Emphasis -> {
//            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
//            node.firstChild?.let { processNode(it) }
//            pop()
//        }
//        is Link -> {
//            pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
//            node.firstChild?.let { processNode(it) }
//            pop()
//        }
//        is Code -> {
//            pushStyle(SpanStyle(
//                fontFamily = FontFamily.Monospace,
//                background = Color.LightGray.copy(alpha = 0.3f)
//            ))
//            append(node.literal ?: "")
//            pop()
//        }
//        is FencedCodeBlock -> {
//            append("\n")
//            pushStyle(SpanStyle(
//                fontFamily = FontFamily.Monospace,
//                background = Color.LightGray.copy(alpha = 0.3f)
//            ))
//            append(node.literal ?: "")
//            pop()
//            append("\n")
//        }
//        else -> {
//            node.firstChild?.let { processNode(it) }
//        }
//    }
//    if (node !is ListItem) {
//        node.next?.let { processNode(it, listLevel) }
//    }
//}

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    keywords: List<String>,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onKeywordClick: (String) -> Unit,
    onHeaderClick: (String) -> Unit = {} // 헤더 클릭 콜백 추가
) {
    val parser = remember { Parser.builder().build() }
    val document = remember(markdown) { parser.parse(markdown) }
    val annotatedString =  remember(document, keywords) {
        document.toAnnotatedString(keywords)
    }

//    Column(modifier = modifier) {
//        Text(
//            text = annotatedString,
//            color = color,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
    Column(modifier = modifier) {
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge.copy(color = color),
            onClick = { offset ->
                annotatedString
                    .getStringAnnotations("CLICKABLE", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        onKeywordClick(annotation.item)
                    }
                // 헤더 클릭 처리
                annotatedString
                    .getStringAnnotations("HEADER", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        onHeaderClick(annotation.item)
                    }
            }
        )
    }
}

private fun Node.toAnnotatedString(keywords: List<String>): AnnotatedString {

    return buildAnnotatedString {
        processNode(this@toAnnotatedString, keywords = keywords)
    }
}

@Composable
private fun CodeBlock(content: String, modifier: Modifier = Modifier) {
    Text(
        text = content,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.bodyMedium
    )
}

private fun AnnotatedString.Builder.processNode(
    node: Node,
    listLevel: Int = 0,
    keywords: List<String> = emptyList()
) {
    when (node) {
        is Heading -> {
            append("\n")
            //헤어의 시작 위치 저장
            val start = length

            pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = when (node.level) {
                        1 -> 24.sp
                        2 -> 20.sp
                        3 -> 18.sp
                        else -> 16.sp
                    }
                )
            )

            //헤더 텍스트를 클릭 가능한 영역으로 만들기
            pushStringAnnotation(
                tag = "HEADER",
                annotation = node.firstChild?.let {
                    when (it) {
                        is Text -> it.literal
                        else -> ""
                    }
                } ?: ""
            )


            node.firstChild?.let { processNode(it, listLevel, keywords) }

            //헤더 클릭 영역 종료
            pop()
            pop()
            append("\n")
        }
        is BulletList -> {
            node.firstChild?.let {
                processNode(it, listLevel + 1, keywords)
            }
        }
        is ListItem -> {
            append("\n")
            append("  ".repeat(listLevel))
            append("• ")
            node.firstChild?.let { processNode(it, listLevel, keywords) }
            node.next?.let { processNode(it, listLevel, keywords) }
        }
        is Paragraph -> {
            if (node.parent !is ListItem) append("\n")
            node.firstChild?.let { processNode(it, listLevel, keywords) }
            if (node.parent !is ListItem) append("\n")
        }
        is Text -> {
            val text = node.literal
            var lastIndex = 0

            // 모든 키워드에 대해 검사
            keywords.forEach { keyword ->
                val pattern = keyword.toRegex(RegexOption.IGNORE_CASE)
                pattern.findAll(text).forEach { result ->
                    // 인덱스 유효성 검사 추가
                    if (result.range.first >= lastIndex && result.range.first < result.range.last + 1) {
                        // 키워드 이전 텍스트 추가
                        append(text.substring(lastIndex, result.range.first))

                        // 클릭 가능한 영역 시작
                        pushStringAnnotation(
                            tag = "CLICKABLE",
                            annotation = keyword
                        )

                        // 키워드를 하이라이트와 함께 추가
                        pushStyle(SpanStyle(background = Color.Yellow.copy(alpha = 0.5f)))
                        append(text.substring(result.range.first, result.range.last + 1))

                        pop()
                        pop()

                        lastIndex = result.range.last + 1
                    }
                }
            }
            // 남은 텍스트 추가
            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        }
        is StrongEmphasis -> {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            node.firstChild?.let { processNode(it, listLevel, keywords) }
            pop()
        }
        is Emphasis -> {
            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
            node.firstChild?.let { processNode(it, listLevel, keywords) }
            pop()
        }
        is Link -> {
            pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            node.firstChild?.let { processNode(it, listLevel, keywords) }
            pop()
        }
        is Code -> {
            pushStyle(SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = Color.LightGray.copy(alpha = 0.3f)
            ))
            append(node.literal ?: "")
            pop()
        }
        is FencedCodeBlock -> {
            append("\n")
            pushStyle(SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = Color.LightGray.copy(alpha = 0.3f)
            ))
            append(node.literal ?: "")
            pop()
            append("\n")
        }
        else -> {
            node.firstChild?.let { processNode(it, listLevel, keywords) }
        }
    }
    if (node !is ListItem) {
        node.next?.let { processNode(it, listLevel, keywords) }
    }
}