package com.manasmalla.draarogyashealthrecord.data

import com.manasmalla.draarogyashealthrecord.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun getAllRecords(): Flow<List<Record>>

    fun getUserRecords(userId: Int): Flow<List<Record>>

    suspend fun addRecord(record: Record)

    suspend fun updateRecord(record: Record)

    suspend fun deleteRecord(record: Record)

}