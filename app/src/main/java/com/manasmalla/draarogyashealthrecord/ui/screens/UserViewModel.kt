package com.manasmalla.draarogyashealthrecord.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    var isDarkTheme = MutableStateFlow(false)
        private set

    var uiState by mutableStateOf(
        UserUiState(
            name = "", age = 0, gender = Gender.Male, metric = listOf()
        )
    )
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun updateAge(age: String) {
        uiState = uiState.copy(age = age.toIntOrNull() ?: 0)
    }

    fun updateGender(gender: Gender) {
        uiState = uiState.copy(gender = gender)
    }

    fun updateMetric(metric: Metrics) {
        val updatedMetricList = uiState.metric.toMutableList()
        if (updatedMetricList.contains(metric)) {
            updatedMetricList.remove(metric)
        } else {
            updatedMetricList.add(metric)
        }
        uiState = uiState.copy(metric = updatedMetricList.toList())
    }

    fun updateHeightUnit(heightUnit: Int) {
        uiState = uiState.copy(heightUnit = heightUnit)
    }

    fun updateWeightUnit(weightUnit: Int) {
        uiState = uiState.copy(weightUnit = weightUnit)
    }

    fun updateTheme() {
        viewModelScope.launch {
            isDarkTheme.emit(isDarkTheme.value)
        }
    }

}