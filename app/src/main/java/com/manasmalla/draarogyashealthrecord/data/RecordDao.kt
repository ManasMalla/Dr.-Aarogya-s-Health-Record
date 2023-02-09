package com.manasmalla.draarogyashealthrecord.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.manasmalla.draarogyashealthrecord.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * FROM record ORDER BY userId ASC")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM record WHERE userId=:userId ORDER BY date ASC")
    fun getUserRecords(userId: Int): Flow<List<Record>>

    @Insert
    suspend fun addRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

}