package com.manasmalla.draarogyashealthrecord.ui

import androidx.compose.ui.graphics.ImageBitmap
import com.manasmalla.draarogyashealthrecord.model.Gender

sealed class ProfileUiState {
    data class Storage(val bitmap: ImageBitmap) : ProfileUiState()
    data class Default(val gender: Gender) : ProfileUiState()
    object Loading : ProfileUiState()
}