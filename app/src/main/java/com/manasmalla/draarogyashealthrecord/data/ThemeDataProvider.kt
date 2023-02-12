package com.manasmalla.draarogyashealthrecord.data

import androidx.compose.runtime.compositionLocalOf

sealed class ThemeDataProvider {
    object Light : ThemeDataProvider()
    object Dark : ThemeDataProvider()
}

val LocalThemeData = compositionLocalOf<ThemeDataProvider> { ThemeDataProvider.Light }