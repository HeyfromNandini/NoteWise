package project.app.notewise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: UserInfoEntity)

    @Query("SELECT * FROM user_info")
    fun getUserInfo(): Flow<List<UserInfoEntity>>

    @Query("DELETE FROM user_info")
    suspend fun deleteAll()
}