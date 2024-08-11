package project.app.notewise.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val t: Any?,
    val title: String?,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
) {
    object HomeScreen : BottomBarScreens(
        Home,
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )

    object AskAIScreen : BottomBarScreens(
        AskAI,
        "Search",
        Icons.Filled.Search,
        Icons.Outlined.Search,
    )

    object CreateNotesScreen : BottomBarScreens(
        CreateNote,
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