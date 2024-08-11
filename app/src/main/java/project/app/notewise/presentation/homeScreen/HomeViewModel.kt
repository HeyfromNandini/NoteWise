package project.app.notewise.presentation.homeScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    val userInfo = databaseRepo.userInfo

}