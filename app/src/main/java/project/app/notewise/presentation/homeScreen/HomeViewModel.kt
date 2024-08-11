package project.app.notewise.presentation.homeScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDatastore: UserDatastore,
    private val apiService: ApiService
) : ViewModel() {


}