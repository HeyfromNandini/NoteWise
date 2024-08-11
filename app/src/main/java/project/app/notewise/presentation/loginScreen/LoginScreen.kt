package project.app.notewise.presentation.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.app.notewise.baseUI.theme.utils.ErrorHandler
import project.app.notewise.baseUI.theme.utils.TextFieldWithIcons
import project.app.notewise.domain.models.SignUp
import kotlin.math.log

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by loginViewModel.authState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        loginViewModel.resetState()
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onLoginSuccess()
            }

            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).errorResponse.message
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .then(
                if (authState is AuthState.Loading) {
                    Modifier.blur(5.dp)
                } else {
                    Modifier
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextFieldWithIcons(
            textValue = "Email",
            placeholder = "Enter your email",
            icon = Icons.Filled.Email,
            mutableText = email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onValueChanged = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithIcons(
            textValue = "Password",
            placeholder = "Enter your password",
            icon = Icons.Filled.Password,
            mutableText = password,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isTrailingVisible = true,
            onTrailingClick = { passwordVisible = !passwordVisible },
            onValueChanged = { password = it },
            ifIsOtp = false,
            isEnabled = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { loginViewModel.signIn(email.text, password.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.filledTonalButtonColors()
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { /* Handle Forgot Password */ }) {
                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            TextButton(onClick = {
                navController.navigate(SignUp)
            }) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    ErrorHandler(
        errorMessage = errorMessage,
        onDismiss = { errorMessage = null },
        coroutineScope = coroutineScope
    )

    if (authState is AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier)
        }
    }
}

