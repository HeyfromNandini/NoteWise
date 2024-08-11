package project.app.notewise.presentation.navController

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import project.app.notewise.R
import project.app.notewise.baseUI.theme.utils.ErrorHandler
import project.app.notewise.domain.models.Home
import project.app.notewise.domain.models.Login


@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navHostController: NavHostController
) {
    val state by viewModel.splashState
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(3000)
        viewModel.signIn()
    }

    LaunchedEffect(state) {
        when (state) {
            is SplashState.NavigateToLogin -> {
                navHostController.popBackStack()
                navHostController.navigate(Login)
            }

            is SplashState.NavigateToHome -> {
                navHostController.popBackStack()
                navHostController.navigate(Home)
            }

            is SplashState.Error -> {
                errorMessage = (state as SplashState.Error).message
            }

            else -> {}
        }
    }

    val items = listOf(
        "Initializing" to Icons.Filled.Settings,
        "Loading resources" to Icons.Filled.CloudDownload,
        "Preparing UI" to Icons.Filled.ViewQuilt,
        "Fetching data" to Icons.Filled.Sync,
        "Authenticating user" to Icons.Filled.VerifiedUser,
        "Connecting to server" to Icons.Filled.Cloud,
        "Syncing preferences" to Icons.Filled.SyncAlt,
        "Finalizing setup" to Icons.Filled.Done,
        "Starting app" to Icons.Filled.PlayArrow,
        "Almost there..." to Icons.Filled.CheckCircle
    )

    var visibleIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (state is SplashState.Loading) {
            for (i in items.indices) {
                visibleIndex = i
                delay(3000)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notewise),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(3000, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(3000, easing = LinearOutSlowInEasing)
            )
        ) {
            SplashItem(text = items[visibleIndex].first, icon = items[visibleIndex].second)
        }

        ErrorHandler(
            errorMessage = errorMessage,
            onDismiss = { errorMessage = null },
            coroutineScope = coroutineScope
        )
    }
}


@Composable
fun SplashItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}



