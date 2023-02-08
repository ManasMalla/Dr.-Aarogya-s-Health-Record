package com.manasmalla.draarogyashealthrecord.data

import android.content.Context


interface AppContainer {
    val userRepository: UserRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val userDao: UserDao by lazy {
        RecordDatabase.getDatabase(context).userDao()
    }


    override val userRepository: UserRepository
        get() = OfflineUserRepository(userDao)

}

