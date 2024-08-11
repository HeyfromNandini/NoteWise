package project.app.notewise.presentation.createNotes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import project.app.notewise.data.createNote.CreateNoteRequest
import project.app.notewise.data.local.UserInfoEntity
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.models.formattingIcons
import project.app.notewise.domain.network.ApiService
import project.app.notewise.presentation.loginScreen.AuthState
import javax.inject.Inject

sealed class CreateNoteState {
    object Idle : CreateNoteState()
    object Loading : CreateNoteState()
    data class Success(val message: String) : CreateNoteState()
    data class Error(val message: String) : CreateNoteState()
    data class Unauthorized(val message: String) : CreateNoteState()
}

@HiltViewModel
class CreateNotesViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _createNoteState = MutableStateFlow<CreateNoteState>(CreateNoteState.Idle)
    val createNoteState: StateFlow<CreateNoteState> = _createNoteState.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email.asStateFlow()

    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?> = _password.asStateFlow()

    var currentIcon by mutableStateOf(formattingIcons[0])
    var title by mutableStateOf("")
    val isDescriptionFocused = mutableStateOf(false)
    val isBottomSheetVisible = mutableStateOf(false)

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?> = _idToken.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            databaseRepo.userInfo.collectLatest {
                it.forEach { userInfo ->
                    _userId.value = userInfo?.localId
                    _idToken.value = userInfo?.idToken
                    _email.value = userInfo?.email
                    _password.value = userInfo?.password
                }
            }
        }
    }

    fun saveNote(createNoteRequest: CreateNoteRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _createNoteState.value = CreateNoteState.Loading
            try {
                val result = apiService.createNote(
                    createNoteRequest, _idToken.value ?: ""
                )
                println("Is loading10 ${result}")
                result.fold(
                    onFailure = {
                        if (it.message?.contains("Unauthorized") == true) {
                            reauthorize()
                            _createNoteState.value = CreateNoteState.Unauthorized(it.message.toString())
                        }
                    },
                    onSuccess = {
                        _isLoading.value = false
                        _createNoteState.value = CreateNoteState.Success(it.message.toString())
                    }
                )
            } catch (e: Exception) {
               println("Is loading2 ${e.message}")
                _createNoteState.value = CreateNoteState.Error(e.message.toString())
            } finally {
                _isLoading.value = false
                _createNoteState.value = CreateNoteState.Idle
            }
        }
    }

    private fun reauthorize() {
        viewModelScope.launch {
            userDatastore.saveIsLoggedIn(false)
            databaseRepo.deleteAll()
        }
    }


}
