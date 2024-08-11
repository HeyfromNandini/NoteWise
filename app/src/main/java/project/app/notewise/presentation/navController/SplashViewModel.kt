package project.app.notewise.presentation.navController


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import javax.inject.Inject

sealed class SplashState {
    object Loading : SplashState()
    object NavigateToLogin : SplashState()
    object NavigateToHome : SplashState()
    data class Error(val message: String) : SplashState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo,
    private val userDatastore: UserDatastore,
) : ViewModel() {

    val splashState = mutableStateOf<SplashState>(SplashState.Loading)

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    init {
        viewModelScope.launch {
            databaseRepo.userInfo.collectLatest {
                it.forEach { userInfo ->
                    email = userInfo?.email ?: ""
                    password = userInfo?.password ?: ""
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val result = apiService.signIn(email, password)
            result.fold(
                onFailure = {
                    if (it.message?.contains("Unauthorized") == true) {
                        withContext(Dispatchers.IO) {
                            reauthorize()
                        }
                        splashState.value = SplashState.NavigateToLogin
                    } else {
                        splashState.value = SplashState.Error(it.message.toString())
                    }
                },
                onSuccess = {
                    splashState.value = SplashState.NavigateToHome
                }
            )
        }
    }

    private fun reauthorize() {
        viewModelScope.launch {
            userDatastore.saveIsLoggedIn(false)
            databaseRepo.deleteAll()
        }
    }
}
