package com.manasmalla.draarogyashealthrecord.ui.screens.home

import com.manasmalla.draarogyashealthrecord.ui.screens.Metrics

data class RecordUiState(
    val measurableMetrics: List<Metrics> = listOf(),
    val measurements: List<String> = listOf(),
    val actionsEnabled: Boolean = false
)

val RecordUiState.isValid: Boolean
    get() = this.measurements.fold(true) { isActive, value ->
        isActive.and(value.isNotBlank()).and(value.toDoubleOrNull() != null)
    }
