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
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.models.formattingIcons
import project.app.notewise.domain.network.ApiService
import javax.inject.Inject

@HiltViewModel
class CreateNotesViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

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
                }
            }
        }
    }

    fun saveNote(createNoteRequest: CreateNoteRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = apiService.createNote(
                    createNoteRequest, _idToken.value ?: ""
                )
                println("Is loading10 ${result}")
            } catch (e: Exception) {
               println("Is loading2 ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
