package com.manasmalla.draarogyashealthrecord

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.manasmalla.draarogyashealthrecord.data.AppContainer
import com.manasmalla.draarogyashealthrecord.data.DefaultAppContainer

class HealthRecordApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        val context = this.applicationContext
        appContainer = DefaultAppContainer(context)
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [HealthRecordApplication].
 */
fun CreationExtras.recordApplication(): HealthRecordApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HealthRecordApplication)