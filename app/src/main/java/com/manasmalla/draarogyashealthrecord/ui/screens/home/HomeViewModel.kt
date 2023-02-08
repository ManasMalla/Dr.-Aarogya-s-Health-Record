package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.isValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(userRepository: UserRepository) : ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(userRepository = recordApplication().appContainer.userRepository)
            }
        }

    }

    init {
        viewModelScope.launch {
            getRecords()
        }
    }

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var userUiState: StateFlow<UserUiState> = userRepository.getCurrentUser()
        .map { it.toUiState() }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(
                STOP_TIMEOUT_MILLISECONDS
            ), UserUiState(name = "User", age = "", gender = Gender.Other)
        )


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

    private suspend fun getRecords() {
        viewModelScope.launch {
            delay(1000)
            uiState = HomeUiState.Empty()
        }
    }

    fun updateAccountDialogVisibility(isVisible: Boolean) {
        Log.d("DialogActivity", "Close: $isVisible")
        when (uiState) {
            is HomeUiState.Empty -> uiState =
                (uiState as HomeUiState.Empty).copy(isAccountDialogOpen = isVisible)

            is HomeUiState.Success -> uiState =
                (uiState as HomeUiState.Success).copy(isAccountDialogOpen = isVisible)

            else -> {}
        }
    }

    fun updateRecordUiState(updatedRecordUiState: RecordUiState) {
        recordUiState = updatedRecordUiState.copy(actionsEnabled = updatedRecordUiState.isValid)
    }

    fun addRecord() {
        //TODO: Add Record to the database
        recordUiState = RecordUiState()

    }

    fun showAccountDialog() {
        updateAccountDialogVisibility(true)
    }

    fun dismissAccountDialog() {
        updateAccountDialogVisibility(false)
    }

}