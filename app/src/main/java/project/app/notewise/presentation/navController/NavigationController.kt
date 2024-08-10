package project.app.notewise.presentation.navController

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.app.notewise.domain.Screens
import project.app.notewise.presentation.createNotes.CreateNoteScreen

@Composable
fun NavigationController(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    isBottomBarVisible: MutableState<Boolean>
) {
    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            Column {
                Text("Home")
            }
        }
        composable(Screens.CreateNotes.route) {
            CreateNoteScreen(paddingValues = paddingValues)
        }
        composable(Screens.AskAI.route) {
            Column {
                Text("Ask AI")
            }
        }

        composable(Screens.Search.route) {
            Column {
                Text("Search")
            }
        }
    }
}
