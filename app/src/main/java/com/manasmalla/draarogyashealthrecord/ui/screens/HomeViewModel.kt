package com.manasmalla.draarogyashealthrecord.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    suspend fun getRecords(){
        viewModelScope.launch {
            delay(1000)
            uiState = HomeUiState.Empty()
        }
    }

    fun onOpenAccountDialog() {
        when(uiState){
            is HomeUiState.Empty -> uiState = (uiState as HomeUiState.Empty).copy(isAccountDialogOpen = true)
            is HomeUiState.Success -> uiState = (uiState as HomeUiState.Success).copy(isAccountDialogOpen = true)
            else -> {}
        }
    }

    fun dismissAccountDialog() {
        when(uiState){
            is HomeUiState.Empty -> uiState = (uiState as HomeUiState.Empty).copy(isAccountDialogOpen = false)
            is HomeUiState.Success -> uiState = (uiState as HomeUiState.Success).copy(isAccountDialogOpen = false)
            else -> {}
        }
    }

}