package project.app.notewise.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import project.app.notewise.data.local.UserInfoDao
import project.app.notewise.data.local.UserInfoEntity

@Database(
    entities = [
        UserInfoEntity::class,
    ], version = 1, exportSchema = false
)
abstract class DatabaseObject : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao

    companion object {
        @Volatile
        private var Instance: DatabaseObject? = null

        fun getInstance(context: Context): DatabaseObject {
            synchronized(this) {
                if (Instance == null) {
                    Instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseObject::class.java,
                            "TravelDatabase"
                        ).build()
                }
            }
            return Instance!!
        }
    }
}