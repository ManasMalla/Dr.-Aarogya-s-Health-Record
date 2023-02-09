package com.manasmalla.draarogyashealthrecord.data.fake

import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    override fun getAllUsers(): Flow<List<User>> = flow {
        emit(
            listOf(
                User(
                    name = "User", age = 18, gender = Gender.Male.name, metric = listOf(Metrics.BMI)
                )
            )
        )
    }

    override fun getUser(userId: Int): Flow<User> = flow {
        emit(User(name = "User", age = 18, gender = Gender.Male.name, metric = listOf(Metrics.BMI)))
    }

    override fun getCurrentUser(): Flow<User> = flow {
        emit(User(name = "User", age = 18, gender = Gender.Male.name, metric = listOf(Metrics.BMI)))
    }

    override fun getUsersCount(): Flow<Int> = flow {
        emit(3)
    }

    override suspend fun registerUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentUser(user: User) {
        TODO("Not yet implemented")
    }
}