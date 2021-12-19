package com.manasmalla.draarogyashealthrecord.model

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manasmalla.draarogyashealthrecord.ui.theme.WindowSizeClass
import com.manasmalla.draarogyashealthrecord.ui.theme.rememberWindowSizeClass

class HealthRecordViewModel() : ViewModel() {
    private val _windowSizeClass:MutableLiveData<WindowSizeClass?> = MutableLiveData(null)
    val windowSizeClass:LiveData<WindowSizeClass?> get() = _windowSizeClass
    fun setWindowSizeClass(userWindowSizeClass: WindowSizeClass){
        _windowSizeClass.value = userWindowSizeClass
    }
}
class HealthRecordViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(HealthRecordViewModel::class.java)){
            return HealthRecordViewModel() as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}