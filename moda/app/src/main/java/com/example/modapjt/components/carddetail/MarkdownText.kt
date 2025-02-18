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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
    onKeywordClick: (String) -> Unit
) {
    val parser = remember { Parser.builder().build() }
    val document = remember(markdown) {
        println("Parsing markdown: $markdown")
        parser.parse(markdown) }

    LaunchedEffect(markdown) {
        // 마크다운 구조 확인을 위한 로깅
        var currentNode = document.firstChild
        while (currentNode != null) {
            println("Node type: ${currentNode.javaClass.simpleName}, Content: ${currentNode}")
            currentNode = currentNode.next
        }
    }

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 6.dp)
    ) {
        document.let { node ->
            MarkdownSection(
                node = node,
                keywords = keywords,
                color = color,
                onKeywordClick = onKeywordClick
            )
        }
    }
}

@Composable
private fun MarkdownSection(
    node: Node,
    keywords: List<String>,
    color: Color,
    onKeywordClick: (String) -> Unit
) {
    when (node) {
        is Heading -> {
            val headingText = remember(node, keywords) {
                buildAnnotatedString {
                    processHeadingNode(node, keywords)
                }
            }

            ClickableText(
                text = headingText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = when {
                            // level 1(#)이면서 이전에 level 1 헤딩이 없는 경우 (첫 번째 # 헤딩)
                            node.level == 1 && !hasPreviousHeading(node) -> 30.dp
                            node.level == 1 -> 50.dp  // # 로 시작하는 헤딩의 경우 위 패딩을 더 크게
                            node.level == 3 -> 20.dp
                            node.previous != null -> 10.dp
                            else -> 0.dp
                        },
                        bottom = 10.dp,
                    ),
//                    .padding(top = if (node.previous != null) 16.dp else 0.dp, bottom = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = color,
                    fontSize = when (node.level) {
//                        1 -> 20.sp
//                        2 -> 18.sp
//                        3 -> 16.sp
//                        else -> 14.sp
                        1 -> 18.sp
                        2 -> 14.sp
                        3 -> 14.sp
                        else -> 12.sp
                    },
                    fontWeight = FontWeight.Bold
                ),
                onClick = { offset ->
                    headingText.getStringAnnotations("CLICKABLE", offset, offset)
                        .firstOrNull()?.let { annotation ->
                            onKeywordClick(annotation.item)
                        }
                }
            )
        }
        is ListItem -> {
            val listItemText = buildAnnotatedString {
                append("• ")  // bullet point 추가
                node.firstChild?.let { child ->
                    processTextContent(child, keywords)
                }
            }
            ClickableText(
                text = listItemText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 2.dp,     // bullet point용 좁은 간격
//                        bottom = 6.dp,    // bullet point용 좁은 간격
                        start = 4.dp
                    ),
                style = MaterialTheme.typography.bodyLarge.copy(color = color),
                onClick = { offset ->
                    listItemText.getStringAnnotations("CLICKABLE", offset, offset)
                        .firstOrNull()?.let { annotation ->
                            onKeywordClick(annotation.item)
                        }
                }
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        is Paragraph -> {
            val paragraphText = buildAnnotatedString {
                processParagraphNode(node, keywords)
            }

            ClickableText(
                text = paragraphText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (node.previous is Heading) 4.dp else 8.dp,
                        bottom = if (node.next is Heading) 4.dp else 8.dp
                    ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = color,
                    lineHeight = 18.sp
                ),
                onClick = { offset ->
                    paragraphText.getStringAnnotations("CLICKABLE", offset, offset)
                        .firstOrNull()?.let { annotation ->
                            onKeywordClick(annotation.item)
                        }
                }
            )
        }
        else -> {
            node.firstChild?.let {
                MarkdownSection(it, keywords, color, onKeywordClick)
            }
        }
    }

    node.next?.let {
        MarkdownSection(it, keywords, color, onKeywordClick)
    }
}

// 이전에 level 2 헤딩이 있는지 확인하는 함수
private fun hasPreviousHeading(node: Node): Boolean {
    var currentNode = node.previous
    while (currentNode != null) {
        if (currentNode is Heading && currentNode.level == 1) {
            return true
        }
        currentNode = currentNode.previous
    }
    return false
}


private fun AnnotatedString.Builder.processHeadingNode(
    node: Node,
    keywords: List<String>
) {
    node.firstChild?.let { processTextContent(it, keywords) }
}

private fun AnnotatedString.Builder.processParagraphNode(
    node: Node,
    keywords: List<String>
) {
    node.firstChild?.let { processTextContent(it, keywords) }
}

val highlightColors = listOf(
    Color(0xFFFFF9C4),  // 연한 노란색
    Color(0xFFB2FFD6),  // 연한 민트색
    Color(0xFFFFCDD2),  // 연한 핑크색
    Color(0xFFE1BEE7)   // 연한 보라색
)

private fun AnnotatedString.Builder.processTextContent(
    node: Node,
    keywords: List<String>
) {
    when (node) {
        is Text -> {
            val text = node.literal
            var lastIndex = 0

            keywords.forEachIndexed  { index, keyword ->
                val pattern = keyword.toRegex(RegexOption.IGNORE_CASE)
                val highlightColor = highlightColors[index % highlightColors.size]

                pattern.findAll(text).forEach { result ->
                    if (result.range.first >= lastIndex && result.range.first < result.range.last + 1) {
                        append(text.substring(lastIndex, result.range.first))

                        pushStringAnnotation(
                            tag = "CLICKABLE",
                            annotation = keyword
                        )

                        // 하이라이트될 부분을 인라인 콘텐츠로 처리
                        pushStyle(SpanStyle(background = highlightColor))
                        append(text.substring(result.range.first, result.range.last + 1))
                        pop()
                        pop()

                        lastIndex = result.range.last + 1
                    }
                }
            }

            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        }
        is StrongEmphasis -> {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            node.firstChild?.let { processTextContent(it, keywords) }
            pop()
        }
        is Emphasis -> {
            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
            node.firstChild?.let { processTextContent(it, keywords) }
            pop()
        }
        // 다른 노드 타입들에 대한 처리...
        else -> {
            node.firstChild?.let { processTextContent(it, keywords) }
        }
    }
    node.next?.let { processTextContent(it, keywords) }
}

//@Composable
//fun MarkdownText(
//    markdown: String,
//    modifier: Modifier = Modifier,
//    keywords: List<String>,
//    color: Color = MaterialTheme.colorScheme.onSurface,
//    onKeywordClick: (String) -> Unit
//) {
//    val parser = remember { Parser.builder().build() }
//    val document = remember(markdown) { parser.parse(markdown) }
//    val annotatedString =  remember(document, keywords) {
//        document.toAnnotatedString(keywords)
//    }
//
////    Column(modifier = modifier) {
////        Text(
////            text = annotatedString,
////            color = color,
////            style = MaterialTheme.typography.bodyLarge
////        )
////    }
//    Column(modifier = modifier) {
//        ClickableText(
//            text = annotatedString,
//            style = MaterialTheme.typography.bodyLarge.copy(color = color),
//            onClick = { offset ->
//                annotatedString
//                    .getStringAnnotations("CLICKABLE", offset, offset)
//                    .firstOrNull()?.let { annotation ->
//                        onKeywordClick(annotation.item)
//                    }
//            }
//        )
//    }
//}
//
//private fun Node.toAnnotatedString(keywords: List<String>): AnnotatedString {
//
//    return buildAnnotatedString {
//        processNode(this@toAnnotatedString, keywords = keywords)
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
//private fun AnnotatedString.Builder.processNode(
//    node: Node,
//    listLevel: Int = 0,
//    keywords: List<String> = emptyList()
//) {
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
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//            pop()
//            // 헤딩 뒤의 개행은 최소화
//            if (node.next != null) append("\n")
//        }
//        is BulletList -> {
//            node.firstChild?.let {
//                processNode(it, listLevel + 1, keywords)
//            }
//        }
//        is ListItem -> {
//            append("\n")
//            append("  ".repeat(listLevel))
//            append("• ")
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//            node.next?.let { processNode(it, listLevel, keywords) }
//        }
//        is Paragraph -> {
//            // 리스트 아이템이 아닐 경우에만 앞에 개행 추가
//            if (node.parent !is ListItem && node.previous != null) append("\n")
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//            // 다음 노드가 있을 경우에만 개행 추가
//            if (node.parent !is ListItem && node.next != null) append("\n")
////            if (node.parent !is ListItem) append("\n")
////            node.firstChild?.let { processNode(it, listLevel, keywords) }
////            if (node.parent !is ListItem) append("\n")
//        }
//        is Text -> {
//            val text = node.literal
//            var lastIndex = 0
//
//            // 모든 키워드에 대해 검사
//            keywords.forEach { keyword ->
//                val pattern = keyword.toRegex(RegexOption.IGNORE_CASE)
//                pattern.findAll(text).forEach { result ->
//                    // 인덱스 유효성 검사 추가
//                    if (result.range.first >= lastIndex && result.range.first < result.range.last + 1) {
//                        // 키워드 이전 텍스트 추가
//                        append(text.substring(lastIndex, result.range.first))
//
//                        // 클릭 가능한 영역 시작
//                        pushStringAnnotation(
//                            tag = "CLICKABLE",
//                            annotation = keyword
//                        )
//
//                        // 키워드를 하이라이트와 함께 추가
//                        pushStyle(SpanStyle(background = Color.Yellow.copy(alpha = 0.5f)))
//                        append(text.substring(result.range.first, result.range.last + 1))
//
//                        pop()
//                        pop()
//
//                        lastIndex = result.range.last + 1
//                    }
//                }
//            }
//            // 남은 텍스트 추가
//            if (lastIndex < text.length) {
//                append(text.substring(lastIndex))
//            }
//        }
//        is StrongEmphasis -> {
//            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//            pop()
//        }
//        is Emphasis -> {
//            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//            pop()
//        }
//        is Link -> {
//            pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
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
//            node.firstChild?.let { processNode(it, listLevel, keywords) }
//        }
//    }
//    if (node !is ListItem) {
//        node.next?.let { processNode(it, listLevel, keywords) }
//    }
//}