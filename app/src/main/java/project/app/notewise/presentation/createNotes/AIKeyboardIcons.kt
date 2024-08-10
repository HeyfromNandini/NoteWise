package project.app.notewise.presentation.createNotes

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
fun KeyboardVisibilityObserver(
    onKeyboardVisibilityChanged: (Boolean) -> Unit
) {
    val rootView = LocalView.current
    val density = LocalDensity.current.density
    val context = LocalContext.current

    val viewTreeObserver = remember { rootView.viewTreeObserver }
    val callback = rememberUpdatedState(onKeyboardVisibilityChanged)

    DisposableEffect(context) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            callback.value(keypadHeight > (200 * density)) // Check if keyboard is visible
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}


@Composable
fun IconRowWhenKeyboardVisible(
    icons: List<ImageVector>,
    onIconClick: (ImageVector) -> Unit
) {
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val keyboardHeight by keyboardHeightAsState()
    val keyboardHeightPx by keyboardHeightAsState()
    val density = LocalDensity.current.density
    val keyboardHeightDp = keyboardHeightPx / density
    println("keyboardHeight: $keyboardHeightDp")

    KeyboardVisibilityObserver { visible ->
        isKeyboardVisible = visible
    }

    if (isKeyboardVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = keyboardHeightDp.dp)

            ) {
                items(icons) { icon ->
                    IconButton(onClick = { onIconClick(icon) }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun keyboardHeightAsState(): State<Int> {
    val keyboardHeight = remember { mutableStateOf(0) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardHeight.value = if (keypadHeight > screenHeight * 0.15) {
                keypadHeight
            } else {
                0
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardHeight
}


