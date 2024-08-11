package project.app.notewise.data.repository

import kotlinx.coroutines.flow.Flow
import project.app.notewise.data.local.UserInfoDao
import project.app.notewise.data.local.UserInfoEntity

class DatabaseRepo(private val userInfoDao: UserInfoDao) {
    suspend fun insertUserInfo(userInfoEntity: UserInfoEntity) {
        userInfoDao.insertUserInfo(userInfoEntity)
    }

    val userInfo: Flow<List<UserInfoEntity?>> = userInfoDao.getUserInfo()

    suspend fun deleteAll() {
        userInfoDao.deleteAll()
    }
}