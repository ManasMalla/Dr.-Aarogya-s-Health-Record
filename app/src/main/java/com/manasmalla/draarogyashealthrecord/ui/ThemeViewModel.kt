package com.manasmalla.draarogyashealthrecord.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var isDark by
    mutableStateOf(false)

    fun updateDarkTheme() {
        isDark = true
    }

    fun updateLightTheme() {
        isDark = false
    }

}