package com.manasmalla.draarogyashealthrecord.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.manasmalla.draarogyashealthrecord.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user ORDER BY is_current_user DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE uId=:userId")
    fun getUser(userId: Int): Flow<User>

    @Query("SELECT COUNT(*) FROM user")
    fun getUsersCount(): Flow<Int>

    @Insert
    suspend fun registerUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE is_current_user=1 UNION SELECT * FROM user WHERE NOT EXISTS (SELECT * FROM user WHERE is_current_user = 1) AND uId=0")
    fun getCurrentUser(): Flow<User>
}