package com.manasmalla.draarogyashealthrecord.ui.screens.home

sealed class HomeUiState{
    abstract val isAccountDialogOpen: Boolean

    object Loading : HomeUiState() {
        override var isAccountDialogOpen: Boolean = false
    }

    data class Success(
        val records: List<String>,
        override val isAccountDialogOpen: Boolean = false
    ) : HomeUiState()

    data class Empty(override val isAccountDialogOpen: Boolean = false) : HomeUiState()
    object Error : HomeUiState() {
        override val isAccountDialogOpen: Boolean
            get() = false
    }
}
