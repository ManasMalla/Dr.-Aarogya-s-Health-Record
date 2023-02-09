package com.manasmalla.draarogyashealthrecord.ui.screens.home

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
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.isValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
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
            userUiState.map { it.metric }.filterNotNull().collectLatest {
                recordUiState = RecordUiState(
                    measurements = List(userUiState.value.metric.size) { "" },
                    measurableMetrics = userUiState.value.metric
                )
            }
        }
    }


    var dialogUiState by mutableStateOf(
        false
    )

    private suspend fun getRecords() {
        viewModelScope.launch {
            delay(1000)
            uiState = HomeUiState.Empty
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
        dialogUiState = true
    }

    fun dismissAccountDialog() {
        dialogUiState = false
    }

}