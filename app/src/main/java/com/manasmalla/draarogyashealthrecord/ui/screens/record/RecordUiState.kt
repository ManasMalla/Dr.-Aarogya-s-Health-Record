package com.manasmalla.draarogyashealthrecord.ui.screens.record

import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.Record
import java.util.Calendar
import java.util.Date


sealed class RecordUiState {

    object Loading : RecordUiState()

    object Error : RecordUiState()

    data class Success(
        val measurableMetrics: List<Metrics> = listOf(),
        val measurements: List<String> = listOf(),
        val date: Date = Calendar.getInstance().time,
        val actionsEnabled: Boolean = false
    ) : RecordUiState()
}


val RecordUiState.Success.isValid: Boolean
    get() = this.measurements.fold(true) { isActive, value ->
        isActive.and(value.isNotBlank()).and(value.toDoubleOrNull() != null)
    }

fun RecordUiState.Success.toRecord(userId: Int): Record {
    val recordMetrics =
        this.measurableMetrics.zip(this.measurements.map {
            it.toDoubleOrNull() ?: 0.0
        }).toMap()
    return Record(
        record = recordMetrics,
        date = this.date,
        userId = userId
    )
}
