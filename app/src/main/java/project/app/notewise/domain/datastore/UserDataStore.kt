package project.app.notewise.domain.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore("pref")

class UserDatastore(private val context: Context) {
    companion object {
        val password = stringPreferencesKey("password")
        val userName = stringPreferencesKey("userName")
        val email = stringPreferencesKey("email")
        val isLoggedIn = booleanPreferencesKey("login")
        val idToken = stringPreferencesKey("idToken")
        val localId = stringPreferencesKey("localId")
    }

    val getPassword: Flow<String> = context.datastore.data.map {
        it[password] ?: ""
    }

    suspend fun savePassword(userPassword: String) {
        context.datastore.edit {
            it[password] = userPassword
        }
    }

    val getUserName: Flow<String> = context.datastore.data.map {
        it[userName] ?: ""
    }

    suspend fun saveUserName(name: String) {
        context.datastore.edit {
            it[userName] = name
        }
    }

    val getEmail: Flow<String> = context.datastore.data.map {
        it[email] ?: ""
    }

    suspend fun saveEmail(userEmail: String) {
        context.datastore.edit {
            it[email] = userEmail
        }
    }

    val getIsLoggedIn: Flow<Boolean> = context.datastore.data.map {
        it[isLoggedIn] ?: false
    }

    suspend fun saveIsLoggedIn(loggedIn: Boolean) {
        context.datastore.edit {
            it[isLoggedIn] = loggedIn
        }
    }

    val getIdToken: Flow<String> = context.datastore.data.map {
        it[idToken] ?: ""
    }

    suspend fun saveIdToken(idTokenValue: String) {
        context.datastore.edit {
            it[idToken] = idTokenValue
        }
    }

    val getLocalId: Flow<String> = context.datastore.data.map {
        it[localId] ?: ""
    }

    suspend fun saveLocalId(localIdValue: String) {
        context.datastore.edit {
            it[localId] = localIdValue
        }
    }

}