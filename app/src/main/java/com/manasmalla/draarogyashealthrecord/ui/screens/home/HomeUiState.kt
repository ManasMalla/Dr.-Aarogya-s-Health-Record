package com.manasmalla.draarogyashealthrecord.ui.screens.home

import com.manasmalla.draarogyashealthrecord.model.Record

sealed class HomeUiState {

    object Loading : HomeUiState()

    data class Success(
        val records: List<Record>
    ) : HomeUiState()

    object Empty : HomeUiState()
    object Error : HomeUiState()
}
