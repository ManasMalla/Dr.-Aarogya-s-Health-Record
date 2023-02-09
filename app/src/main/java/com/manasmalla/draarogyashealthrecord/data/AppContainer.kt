package com.manasmalla.draarogyashealthrecord.data

import android.content.Context


interface AppContainer {
    val userRepository: UserRepository
    val recordRepository: RecordRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val userDao: UserDao by lazy {
        RecordDatabase.getDatabase(context).userDao()
    }

    private val recordDao: RecordDao by lazy {
        RecordDatabase.getDatabase(context).recordDao()
    }


    override val userRepository: UserRepository
        get() = OfflineUserRepository(userDao)

    override val recordRepository: RecordRepository
        get() = OfflineRecordRepository(recordDao)

}

