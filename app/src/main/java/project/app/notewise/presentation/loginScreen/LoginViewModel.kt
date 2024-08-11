package project.app.notewise.presentation.loginScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.app.notewise.data.ErrorResponse
import project.app.notewise.data.local.UserInfoEntity
import project.app.notewise.data.login.SignInResponse
import project.app.notewise.data.login.SignUpRequest
import project.app.notewise.data.login.VerifyEmailRequest
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import java.io.IOException
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val signInResponse: SignInResponse) : AuthState()
    data class EmailVerificationSuccess(val message: String) : AuthState()
    object EmailVerificationPending : AuthState()
    data class Error(val errorResponse: ErrorResponse) : AuthState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _showVerifyEmailScreen = MutableStateFlow(false)
    val showVerifyEmailScreen: StateFlow<Boolean> = _showVerifyEmailScreen.asStateFlow()

    private val _verifyEmailRequest = MutableStateFlow<VerifyEmailRequest?>(null)
    val verifyEmailRequest: StateFlow<VerifyEmailRequest?> = _verifyEmailRequest.asStateFlow()


    var username by mutableStateOf(TextFieldValue())
    var email by mutableStateOf(TextFieldValue())
    var password by mutableStateOf(TextFieldValue())


    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = apiService.signIn(email, password)
            _authState.value = result.fold(
                onSuccess = {
                    withContext(Dispatchers.IO) {
                        saveUserDetailsToDataStore(it, email, password)
                    }
                    AuthState.Success(it)
                },
                onFailure = { handleApiError(it) }
            )
        }
    }

    private fun saveUserDetailsToDataStore(signInResponse: SignInResponse, email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseRepo.insertUserInfo(
                    UserInfoEntity(
                        email = email,
                        idToken = signInResponse.idToken ?: "",
                        localId = signInResponse.userId ?: "",
                        password = password,
                        userName = ""
                    )
                )
                userDatastore.saveIsLoggedIn(true)
            }
            println("Value is10 ${userDatastore.getIdToken.first()}")
            println("Value is10 ${userDatastore.getLocalId.first()}")
        }
    }

    fun signUp(signUpRequest: SignUpRequest) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = apiService.signUp(signUpRequest)
            _authState.value = result.fold(
                onSuccess = {
                    _showVerifyEmailScreen.value = true
                    updateVerifyEmailRequest(
                        VerifyEmailRequest(
                            idToken = it.idToken
                        )
                    )
                    _authState.value = AuthState.EmailVerificationPending
                    AuthState.EmailVerificationPending
                },
                onFailure = { handleApiError(it) }
            )
        }
    }

    fun verifyEmail(verifyEmailRequest: VerifyEmailRequest) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = apiService.verifyEmail(verifyEmailRequest, email.text)
            _authState.value = result.fold(
                onSuccess = { AuthState.EmailVerificationSuccess("Email verified successfully!") },
                onFailure = { handleApiError(it) }
            )
        }
    }

    fun resetState() {
        _showVerifyEmailScreen.value = false
        _verifyEmailRequest.value = null
        username = TextFieldValue()
        email = TextFieldValue()
        password = TextFieldValue()
        _authState.value = AuthState.Idle
    }

    fun updateVerifyEmailRequest(verifyEmailRequest: VerifyEmailRequest) {
        _verifyEmailRequest.value = verifyEmailRequest
    }

    private fun handleApiError(throwable: Throwable): AuthState.Error {
        val errorResponse = when (throwable) {
            is IOException -> ErrorResponse(
                error = null,
                message = "Network error occurred",
                success = false
            )

            else -> ErrorResponse(
                error = null,
                message = throwable.message ?: "Unknown error occurred",
                success = false
            )
        }
        return AuthState.Error(errorResponse)
    }


}
