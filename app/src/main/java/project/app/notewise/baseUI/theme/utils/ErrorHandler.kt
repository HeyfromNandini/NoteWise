package project.app.notewise.baseUI.theme.utils


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorHandler(
    errorMessage: String?,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    if (!errorMessage.isNullOrEmpty()) {
        LaunchedEffect(errorMessage) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMessage)
                onDismiss()
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = { Text(text = snackbarData.visuals.message, color = Color.White) },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            )
        }
    )
}
