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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val parser = remember { Parser.builder().build() }
    val document = remember(markdown) { parser.parse(markdown) }
    val annotatedString = remember(document) { document.toAnnotatedString() }

    Column(modifier = modifier) {
        Text(
            text = annotatedString,
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun Node.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        processNode(this@toAnnotatedString)
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

private fun AnnotatedString.Builder.processNode(node: Node, listLevel: Int = 0) {
    when (node) {
        is Heading -> {
            append("\n")
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
            node.firstChild?.let { processNode(it) }
            pop()
            append("\n")
        }
        is BulletList -> {
            node.firstChild?.let {
                processNode(it, listLevel + 1)
            }
        }
        is ListItem -> {
            append("\n")
            append("  ".repeat(listLevel))
            append("• ")
            node.firstChild?.let { processNode(it, listLevel) }
            node.next?.let { processNode(it, listLevel) }
        }
        is Paragraph -> {
            if (node.parent !is ListItem) append("\n")
            node.firstChild?.let { processNode(it) }
            if (node.parent !is ListItem) append("\n")
        }
        is Text -> {
            append(node.literal)
        }
        is StrongEmphasis -> {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            node.firstChild?.let { processNode(it) }
            pop()
        }
        is Emphasis -> {
            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
            node.firstChild?.let { processNode(it) }
            pop()
        }
        is Link -> {
            pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            node.firstChild?.let { processNode(it) }
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
            node.firstChild?.let { processNode(it) }
        }
    }
    if (node !is ListItem) {
        node.next?.let { processNode(it, listLevel) }
    }
}