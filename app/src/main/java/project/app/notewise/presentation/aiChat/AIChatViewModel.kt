package project.app.notewise.presentation.aiChat

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
import javax.inject.Inject

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?> = _idToken.asStateFlow()

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

    fun searchNotes() {
        viewModelScope.launch {
            val result = apiService.aiChat(
                SearchRequest(
                    content = "Why does Lakshman love business ?",
                    namespace = _userId.value,
                ),
                _idToken.value ?: ""
            )
        }
    }
}