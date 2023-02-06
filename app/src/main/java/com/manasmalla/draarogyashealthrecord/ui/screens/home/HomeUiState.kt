package com.manasmalla.draarogyashealthrecord.ui.screens.home

sealed class HomeUiState{
    object Loading: HomeUiState()
    data class Success(val records: List<String>, val isAccountDialogOpen: Boolean = false): HomeUiState()
    data class Empty(val isAccountDialogOpen: Boolean = false): HomeUiState()
    object Error: HomeUiState()
}
