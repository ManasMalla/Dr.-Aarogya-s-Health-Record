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
import com.manasmalla.draarogyashealthrecord.data.RecordRepository
import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Record
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.isValid
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(val userRepository: UserRepository, val recordRepository: RecordRepository) :
    ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    userRepository = recordApplication().appContainer.userRepository,
                    recordApplication().appContainer.recordRepository
                )
            }
        }

    }

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var userUiState: StateFlow<UserUiState> =
        userRepository.getCurrentUser().map { it.toUiState() }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(
                STOP_TIMEOUT_MILLISECONDS
            ), UserUiState(name = "User", age = "", gender = Gender.Other)
        )
        private set

    var recordUiState by mutableStateOf(
        RecordUiState(
            measurements = List(userUiState.value.metric.size) { "" },
            measurableMetrics = userUiState.value.metric
        )
    )

    init {
        viewModelScope.launch {
            launch {
                userUiState.map { it.metric }.filterNotNull().collectLatest {
                    recordUiState = RecordUiState(
                        measurements = List(userUiState.value.metric.size) { "" },
                        measurableMetrics = userUiState.value.metric
                    )
                }
            }
            launch {
                userRepository.getCurrentUser().collectLatest {
                    uiState = HomeUiState.Loading
                    getRecords()
                }
            }
        }
    }


    var dialogUiState by mutableStateOf(
        false
    )

    private suspend fun getRecords() {
        viewModelScope.launch {
            recordRepository.getUserRecords(userRepository.getCurrentUser().map { it.uId }.first())
                .collectLatest {
                    uiState = if (it.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(it)
                    }
                }
        }
    }

    fun updateRecordUiState(updatedRecordUiState: RecordUiState) {
        recordUiState = updatedRecordUiState.copy(actionsEnabled = updatedRecordUiState.isValid)
    }

    fun addRecord() {
        viewModelScope.launch {
            val recordMetrics = recordUiState.measurableMetrics.zip(recordUiState.measurements.map {
                it.toDoubleOrNull() ?: 0.0
            }).toMap()
            val record = Record(
                record = recordMetrics,
                date = Calendar.getInstance().time,
                userId = userRepository.getCurrentUser().map { it.uId }.first()
            )
            Log.d("HomeViewModel", "RecordUiStateToRecord: $record")
            recordRepository.addRecord(record)
        }
        recordUiState = RecordUiState()

    }

    fun showAccountDialog() {
        dialogUiState = true
    }

    fun dismissAccountDialog() {
        dialogUiState = false
    }

}