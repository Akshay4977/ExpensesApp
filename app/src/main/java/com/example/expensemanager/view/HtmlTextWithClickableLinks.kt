package com.example.expensemanager.view

import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HtmlTextWithClickableLinks(
    html: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Parse HTML to Spanned
    val spanned: Spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)

    // Convert Spanned to AnnotatedString
    val annotatedString = buildAnnotatedString {
        val text = spanned.toString()
        val spans = spanned.getSpans(0, spanned.length, Any::class.java)

        var cursor = 0
        spans.forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)

            // Append plain text before the styled text
            if (cursor < start) append(text.substring(cursor, start))

            // Handle bullet points
            if (span is android.text.style.BulletSpan) {
                append("\u2022 ")
            }

            // Apply styles and handle links
            if (span is android.text.style.StyleSpan || span is android.text.style.UnderlineSpan || span is android.text.style.URLSpan) {
                val spanStyle = when (span) {
                    is android.text.style.StyleSpan -> when (span.style) {
                        android.graphics.Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
                        android.graphics.Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
                        else -> null
                    }

                    is android.text.style.UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
                    is android.text.style.URLSpan -> SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
                    )

                    else -> null
                }

                if (spanStyle != null) {
                    pushStyle(spanStyle)
                }

                append(text.substring(start, end))

                if (spanStyle != null) {
                    pop()
                }

                // Add annotation for URL spans
                if (span is android.text.style.URLSpan) {
                    addStringAnnotation(
                        tag = "URL",
                        annotation = span.url,
                        start = this.length - (end - start),
                        end = this.length
                    )
                }
            } else {
                append(text.substring(start, end))
            }

            cursor = end
        }

        // Append remaining plain text
        if (cursor < text.length) append(text.substring(cursor))
    }

    // Render ClickableText
    ClickableText(
        text = annotatedString,
        modifier = modifier.padding(8.dp),
        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
        onClick = { offset ->
            val annotations =
                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
            if (annotations.isNotEmpty()) {
                val url = annotations[0].item
                Toast.makeText(context, "Clicked URL: $url", Toast.LENGTH_SHORT).show()
                // You can also handle navigation or opening the URL here
            }
        }
    )
}


@Composable
fun HtmlTextDemo() {
    val text = "Here is a <b>bold</b> text, <i>italic</i>, and an <a href='https://www.example.com'>example link</a>. Here is another <a href='https://www.google.com'>Google link</a> with more text."
    val htmlContent = text.trimIndent()

    HtmlTextWithClickableLinks(html = htmlContent)
}