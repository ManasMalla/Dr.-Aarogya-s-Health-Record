package com.manasmalla.draarogyashealthrecord.ui.screens.home

sealed class HomeUiState {

    object Loading : HomeUiState()

    data class Success(
        val records: List<String>
    ) : HomeUiState()

    object Empty : HomeUiState()
    object Error : HomeUiState()
}
