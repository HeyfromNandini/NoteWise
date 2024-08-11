package project.app.notewise.presentation.loginScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import project.app.notewise.baseUI.theme.utils.ErrorHandler
import project.app.notewise.baseUI.theme.utils.TextFieldWithIcons
import project.app.notewise.data.login.SignUpRequest
import project.app.notewise.domain.models.SignUp

@Composable
fun SignUpScreen(viewModel: LoginViewModel, onSignUpSuccess: () -> Unit) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val showVerifyEmailScreen by viewModel.showVerifyEmailScreen.collectAsStateWithLifecycle()
    val verifyEmailRequest by viewModel.verifyEmailRequest.collectAsStateWithLifecycle()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.EmailVerificationSuccess -> {
                errorMessage = "Email Verification Success"
                viewModel.resetState()
                onSignUpSuccess()
            }

            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).errorResponse.message
            }

            else -> Unit
        }
    }

    AnimatedVisibility(
        visible = !showVerifyEmailScreen,
        enter = slideInHorizontally(initialOffsetX = {-it}) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it}) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                text = "Sign Up",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextFieldWithIcons(
                textValue = "Username",
                placeholder = "Enter your username",
                icon = Icons.Filled.Person,
                mutableText = viewModel.username,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onValueChanged = { viewModel.username = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithIcons(
                textValue = "Email",
                placeholder = "Enter your email",
                icon = Icons.Filled.Email,
                mutableText = viewModel.email,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onValueChanged = { viewModel.email = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithIcons(
                textValue = "Password",
                placeholder = "Enter your password",
                icon = Icons.Filled.Lock,
                mutableText = viewModel.password,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isTrailingVisible = true,
                trailingIcon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                onTrailingClick = { passwordVisible = !passwordVisible },
                onValueChanged = { viewModel.password = it },
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.signUp(
                        SignUpRequest(
                            username = viewModel.username.text,
                            email = viewModel.email.text,
                            password = viewModel.password.text
                        )
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { /* Handle Forgot Password */ }) {
                    Text(
                        text = "Already Registered?",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(onClick = {
                   onSignUpSuccess()
                }) {
                    Text(
                        text = "Login",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = showVerifyEmailScreen,
        enter = slideInHorizontally(initialOffsetX = {-it}) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it}) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Email Verification",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Please verify your email",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We have sent a verification link to your email address. Please check your inbox and verify your email.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    verifyEmailRequest?.let {
                        viewModel.verifyEmail(
                            it
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text(
                    text = "I Have Verified My Email",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
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

