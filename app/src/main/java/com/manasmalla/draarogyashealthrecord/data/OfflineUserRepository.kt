package com.manasmalla.draarogyashealthrecord.data

import com.manasmalla.draarogyashealthrecord.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class OfflineUserRepository(
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    override fun getUser(userId: Int): Flow<User> = userDao.getUser(userId = userId)

    override fun getCurrentUser(): Flow<User> = userDao.getCurrentUser()

    override fun getUsersCount(): Flow<Int> = userDao.getUsersCount()

    override suspend fun registerUser(user: User) = userDao.registerUser(user = user)

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user = user)

    override suspend fun updateUser(user: User) = userDao.updateUser(user = user)

    override suspend fun setCurrentUser(user: User) {
        val currentUser = userDao.getCurrentUser().first()
        userDao.updateUser(user = user.copy(isCurrentUser = true))
        userDao.updateUser(currentUser.copy(isCurrentUser = false))
    }
}