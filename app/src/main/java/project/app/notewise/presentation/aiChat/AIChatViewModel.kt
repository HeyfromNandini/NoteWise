package project.app.notewise.presentation.aiChat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.data.search.SearchRequest
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import project.app.notewise.presentation.createNotes.CreateNoteState
import javax.inject.Inject

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _createNoteState = MutableStateFlow<CreateNoteState>(CreateNoteState.Idle)
    val createNoteState: StateFlow<CreateNoteState> = _createNoteState.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?> = _idToken.asStateFlow()

    var message by mutableStateOf("")

    init {
        viewModelScope.launch {
            databaseRepo.userInfo.collectLatest {
                it.forEach { userInfo ->
                    _userId.value = userInfo?.localId
                    _idToken.value = userInfo?.idToken
                }
            }
        }
    }

    fun searchNotes(question: String) {
        _createNoteState.value = CreateNoteState.Loading
        viewModelScope.launch {
            try {
                val result = apiService.aiChat(
                    SearchRequest(
                        content = question,
                        namespace = _userId.value,
                    ),
                    _idToken.value ?: ""
                )
                message = ""
                result.fold(
                    onFailure = {
                        if (it.message?.contains("Unauthorized") == true) {
                            reauthorize()
                            _createNoteState.value =
                                CreateNoteState.Unauthorized(it.message.toString())
                        } else {
                            _createNoteState.value = CreateNoteState.Error(it.message.toString())
                        }
                    },
                    onSuccess = {
                        _createNoteState.value = CreateNoteState.Success("")
                    }
                )
            } catch (e: Exception) {
                _createNoteState.value = CreateNoteState.Error(e.message.toString())
            } finally {
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