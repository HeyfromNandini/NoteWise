package project.app.notewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import project.app.notewise.baseUI.theme.bottomBar.BottomBar
import project.app.notewise.baseUI.theme.ui.NoteWiseTheme
import project.app.notewise.baseUI.theme.utils.GlobalProgressBar
import project.app.notewise.domain.models.AskAI
import project.app.notewise.domain.models.CreateNote
import project.app.notewise.domain.models.Home
import project.app.notewise.domain.models.Search
import project.app.notewise.presentation.createNotes.CreateNotesViewModel
import project.app.notewise.presentation.navController.NavigationController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteWiseTheme {
                val viewModel = hiltViewModel<CreateNotesViewModel>()
                val isBottomBarVisible = remember { mutableStateOf(true) }
                val navController = rememberNavController()
                // Get the current back stack entry
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                when (navBackStackEntry?.destination?.route?.substringAfterLast(".")) {
                    Home.toString().substringAfterLast(".").substringBefore("@") -> {
                        isBottomBarVisible.value = true
                    }

                    AskAI.toString().substringAfterLast(".").substringBefore("@") -> {
                        isBottomBarVisible.value = true
                    }

                    CreateNote.toString().substringAfterLast(".").substringBefore("@") -> {
                        isBottomBarVisible.value = true
                    }

                    else -> {
                        isBottomBarVisible.value = false
                    }
                }

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            isBottomBarVisible = isBottomBarVisible
                        )
                    },
                    topBar = {
                        GlobalProgressBar(viewModel = viewModel)
                    }
                ) {
                    NavigationController(
                        navHostController = navController,
                        paddingValues = it,
                        isBottomBarVisible = isBottomBarVisible,
                        createNotesViewModel = viewModel
                    )
                }
            }
        }
    }
}

