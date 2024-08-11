package project.app.notewise.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState

data class FormattingEvent(
    val icon: ImageVector,
    val title: String,
    val isSelected: Boolean = false,
    val onClick: (RichTextState) -> Unit
)


val formattingIcons = listOf(
    FormattingEvent(Icons.Default.FormatBold, "Bold") {
        it.removeSpanStyle(it.currentSpanStyle)
        it.addSpanStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold
            )
        )
    },
    FormattingEvent(Icons.Default.FormatItalic, "Italic") {
        it.removeSpanStyle(it.currentSpanStyle)
        it.addSpanStyle(
            SpanStyle(
                fontStyle = FontStyle.Italic
            )
        )

    },
    FormattingEvent(Icons.Default.FormatUnderlined, "Underline") {
        it.removeSpanStyle(it.currentSpanStyle)
        it.addSpanStyle(
            SpanStyle(
                textDecoration = TextDecoration.Underline
            )
        )

    },
)