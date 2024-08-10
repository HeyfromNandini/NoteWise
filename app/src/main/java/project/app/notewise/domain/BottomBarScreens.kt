package project.app.notewise.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val route: String?,
    val title: String?,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
) {
    object HomeScreen : BottomBarScreens(
        Screens.Home.route,
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )

    object AskAIScreen : BottomBarScreens(
        Screens.Search.route,
        "Search",
        Icons.Filled.Search,
        Icons.Outlined.Search,
    )

    object CreateNotesScreen : BottomBarScreens(
        Screens.CreateNotes.route,
        "Create Notes",
        Icons.Filled.Create,
        Icons.Outlined.Create
    )
}

val items = listOf(
    BottomBarScreens.HomeScreen,
    BottomBarScreens.AskAIScreen,
    BottomBarScreens.CreateNotesScreen
)