package project.app.notewise.domain.models

import androidx.compose.ui.graphics.Color
import project.app.notewise.R

data class MarqueeItem(
    val name: String,
    val color: Color,
    val icon: Int? = null,
)

val dummyMarqueeItem = listOf(
    MarqueeItem(
        name = "Gemini",
        color = Color.Yellow,
        icon = R.drawable.ic_gemini
    ),
    MarqueeItem(
        name = "Smart",
        color = Color.Cyan,
        icon = R.drawable.ic_bulb
    ),
    MarqueeItem(
        name = "Note Wise",
        color = Color.Green.copy(0.5f),
        icon = R.drawable.ic_notewise
    ),
    MarqueeItem(
        name = "Voice",
        color = Color(0xFFF0CB5E),
        icon = R.drawable.ic_voice
    ),
    MarqueeItem(
        name = "AI Chat",
        color = Color.Blue,
        icon = R.drawable.ic_aichat
    ),
)