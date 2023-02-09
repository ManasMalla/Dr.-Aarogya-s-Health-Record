package com.manasmalla.draarogyashealthrecord.data

import com.manasmalla.draarogyashealthrecord.model.Record
import kotlinx.coroutines.flow.Flow

class OfflineRecordRepository(private val recordDao: RecordDao) : RecordRepository {

    override fun getAllRecords(): Flow<List<Record>> = recordDao.getAllRecords()

    override fun getUserRecords(userId: Int): Flow<List<Record>> =
        recordDao.getUserRecords(userId = userId)

    override suspend fun addRecord(record: Record) = recordDao.addRecord(record = record)

    override suspend fun updateRecord(record: Record) = recordDao.updateRecord(record = record)

    override suspend fun deleteRecord(record: Record) = recordDao.deleteRecord(record = record)

}