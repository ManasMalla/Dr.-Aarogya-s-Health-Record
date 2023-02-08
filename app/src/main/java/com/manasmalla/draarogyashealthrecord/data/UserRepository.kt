package com.manasmalla.draarogyashealthrecord.data

import com.manasmalla.draarogyashealthrecord.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<User>>

    fun getUser(userId: Int): Flow<User>

    fun getCurrentUser(): Flow<User>

    fun getUsersCount(): Flow<Int>

    suspend fun registerUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun setCurrentUser(user: User)

}