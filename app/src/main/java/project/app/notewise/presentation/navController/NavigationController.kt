package project.app.notewise.presentation.navController

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.collectLatest
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.models.AskAI
import project.app.notewise.domain.models.CreateNote
import project.app.notewise.domain.models.Home
import project.app.notewise.domain.models.Login
import project.app.notewise.domain.models.Onboarding
import project.app.notewise.domain.models.Profile
import project.app.notewise.domain.models.Search
import project.app.notewise.domain.models.SignUp
import project.app.notewise.presentation.aiChat.AIChatViewModel
import project.app.notewise.presentation.createNotes.CreateNoteScreen
import project.app.notewise.presentation.loginScreen.LoginScreen
import project.app.notewise.presentation.loginScreen.LoginViewModel
import project.app.notewise.presentation.loginScreen.SignUpScreen
import project.app.notewise.presentation.loginScreen.WOnBoardingScreen

@Composable
fun NavigationController(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    isBottomBarVisible: MutableState<Boolean>
) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val aiChatViewModel = hiltViewModel<AIChatViewModel>()
    val context = LocalContext.current
    val datastore = UserDatastore(context)
    val isLoggedIn = datastore.getIsLoggedIn.collectAsState(initial = false)
    var idToken by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        datastore.getIdToken.collectLatest {
            println("Value is11a $it")
            idToken = it
        }
    }

    NavHost(navHostController, startDestination = if (isLoggedIn.value) Home else Login) {
        composable<Home> {
            Column {
                Text("Home")
            }
        }

        composable<CreateNote> {
            CreateNoteScreen(paddingValues = paddingValues)
        }

        composable<AskAI> {
            Column {
                Button(onClick = {
                    aiChatViewModel.searchNotes()
                }) {
                    Text("Ask AI")
                }
            }
        }

        composable<Search> {
            Column {
                Text("Search")
            }
        }

        composable<Onboarding> {
            WOnBoardingScreen(paddingValues = paddingValues, navHostController = navHostController)
        }

        composable<Login> {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navHostController,
                onLoginSuccess = {

                }
            )
        }

        composable<Profile> {
            Column {
                Text("Profile")
            }
        }

        composable<SignUp> {
            SignUpScreen(viewModel = loginViewModel) {
                navHostController.navigate(Login)
            }
        }
    }
}

