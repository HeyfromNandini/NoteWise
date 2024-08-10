package project.app.notewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import project.app.notewise.baseUI.theme.bottomBar.BottomBar
import project.app.notewise.baseUI.theme.ui.NoteWiseTheme
import project.app.notewise.domain.Screens
import project.app.notewise.presentation.navController.NavigationController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteWiseTheme {
                val isBottomBarVisible = remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                when (navBackStackEntry?.destination?.route) {
                    Screens.Home.route -> {
                        isBottomBarVisible.value = true
                    }

                    Screens.CreateNotes.route -> {
                        isBottomBarVisible.value = false
                    }

                    Screens.Search.route -> {
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
                    }
                ) {
                    NavigationController(
                        navHostController = navController,
                        paddingValues = it,
                        isBottomBarVisible = isBottomBarVisible
                    )
                }
            }
        }
    }
}

