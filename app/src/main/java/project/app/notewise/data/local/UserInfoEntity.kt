package project.app.notewise.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoEntity(
    val email: String,
    val password: String,
    val userName: String,
    val idToken: String,
    @PrimaryKey
    val localId: String
)
