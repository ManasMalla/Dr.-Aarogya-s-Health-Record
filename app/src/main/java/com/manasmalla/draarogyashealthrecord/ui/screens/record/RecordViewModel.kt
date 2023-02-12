package com.manasmalla.draarogyashealthrecord.ui.screens.record

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasmalla.draarogyashealthrecord.data.RecordRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.unit
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.util.splitCamelCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RecordViewModel(val recordRepository: RecordRepository, val context: Context) : ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RecordViewModel(
                    recordRepository = recordApplication().appContainer.recordRepository,
                    context = recordApplication().applicationContext
                )
            }
        }

    }

    var uiState: RecordUiState by mutableStateOf(
        RecordUiState.Loading
    )

    var selectedRecordId by mutableStateOf(
        -1
    )

    fun getRecordForId(id: Int) {
        selectedRecordId = id
        viewModelScope.launch {
            recordRepository.getRecord(id).filterNotNull().map {
                RecordUiState.Success(
                    measurableMetrics = it.record.keys.toList(),
                    measurements = it.record.values.toList().map { it.toString() },
                    date = it.date,
                    actionsEnabled = false
                )
            }.collectLatest {
                uiState = it
            }
        }
    }

    fun deleteRecord() {
        viewModelScope.launch {
            val record = recordRepository.getRecord(selectedRecordId).first()
            recordRepository.deleteRecord(record)
        }
    }

    fun enableEditRecord() {
        when (uiState) {
            is RecordUiState.Success -> uiState =
                (uiState as RecordUiState.Success).copy(actionsEnabled = true)

            else -> {}
        }
    }

    fun updateEditRecord() {
        viewModelScope.launch {
            val record = recordRepository.getRecord(selectedRecordId).first()
            when (uiState) {
                is RecordUiState.Success -> {
                    val uiState = (uiState as RecordUiState.Success).copy(actionsEnabled = false)
                    this@RecordViewModel.uiState = uiState
                    val updatedRecord = uiState.toRecord(record.userId).copy(id = record.id)
                    recordRepository.updateRecord(updatedRecord)
                }

                else -> {}
            }
        }
    }

    fun updateRecordUiState(updatedRecordUiState: RecordUiState.Success) {
        uiState = updatedRecordUiState
    }

    fun shareRecord(user: StateFlow<UserUiState>) {
        when (val shareUiState = uiState) {
            is RecordUiState.Success -> {
                viewModelScope.launch {
                    val sendIntent: Intent = Intent().apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        action = Intent.ACTION_SEND
                        val currentUser = user.filter { it.metric.isNotEmpty() }.first()
                        val text =
                            "${currentUser.name} is sharing ${if (currentUser.gender == Gender.Male) "his" else "her"} health metrics which ${if (currentUser.gender == Gender.Male) "he" else "she"} recorded on ${
                                SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                                ).format(shareUiState.date)
                            }:"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "$text \n\n${
                                shareUiState.measurableMetrics.zip(shareUiState.measurements)
                                    .joinToString("\n") { "${it.first.name.splitCamelCase}: ${it.second} ${it.first.unit}" }
                            }"
                        )
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, "Share Record")
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(shareIntent)
                }
            }

            else -> {}
        }
    }

}