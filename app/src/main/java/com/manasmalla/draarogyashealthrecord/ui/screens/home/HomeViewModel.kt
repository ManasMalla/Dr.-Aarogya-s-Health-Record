package com.manasmalla.draarogyashealthrecord.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manasmalla.draarogyashealthrecord.ui.screens.Metrics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    val measurableMetrics = listOf(
        Metrics.Weight,
        Metrics.Calories,
        Metrics.FatPercentage,
        Metrics.WaterPercentage,
        Metrics.MuscleMass,
        Metrics.BoneMass,
        Metrics.BMI
    )
    var recordUiState by
    mutableStateOf(
        RecordUiState(
            measurements = List(measurableMetrics.size) { "" },
            measurableMetrics = measurableMetrics
        )
    )

    suspend fun getRecords() {
        viewModelScope.launch {
            delay(1000)
            uiState = HomeUiState.Empty()
        }
    }

    fun onOpenAccountDialog() {
        when (uiState) {
            is HomeUiState.Empty -> uiState =
                (uiState as HomeUiState.Empty).copy(isAccountDialogOpen = true)

            is HomeUiState.Success -> uiState =
                (uiState as HomeUiState.Success).copy(isAccountDialogOpen = true)

            else -> {}
        }
    }

    fun dismissAccountDialog() {
        when (uiState) {
            is HomeUiState.Empty -> uiState =
                (uiState as HomeUiState.Empty).copy(isAccountDialogOpen = false)

            is HomeUiState.Success -> uiState =
                (uiState as HomeUiState.Success).copy(isAccountDialogOpen = false)

            else -> {}
        }
    }

    fun updateRecordUiState(measurements: List<String>) {

        recordUiState.copy(measurements = measurements).also { updatedRecordUiState ->
            recordUiState = updatedRecordUiState.copy(actionsEnabled = updatedRecordUiState.isValid)
        }

    }

    fun addRecord() {
        //TODO: Add Record to the database
        recordUiState = RecordUiState()

    }

}