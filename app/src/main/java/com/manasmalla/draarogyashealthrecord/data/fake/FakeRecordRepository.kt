package com.manasmalla.draarogyashealthrecord.data.fake

import com.manasmalla.draarogyashealthrecord.data.RecordRepository
import com.manasmalla.draarogyashealthrecord.model.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class FakeRecordRepository: RecordRepository {
    override fun getAllRecords(): Flow<List<Record>> = flow {
        emit(listOf())
    }

    override fun getUserRecords(userId: Int): Flow<List<Record>> = flow {
        emit(listOf())
    }

    override fun getRecord(id: Int): Flow<Record> = flow {
        emit(Record(record = mapOf(), date = Calendar.getInstance().time, userId = 0))
    }

    override suspend fun addRecord(record: Record) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecord(record: Record) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecord(record: Record) {
        TODO("Not yet implemented")
    }
}