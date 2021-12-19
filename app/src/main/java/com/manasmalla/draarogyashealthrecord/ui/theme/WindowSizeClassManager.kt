package com.manasmalla.draarogyashealthrecord.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator
import com.manasmalla.draarogyashealthrecord.ui.theme.WindowSizeClass.*


enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

fun WindowSizeClass?.stringValue():String{
    return when(this){
        COMPACT -> "Compact Window"
        MEDIUM -> "Medium Window"
        EXPANDED -> "Expanded Window"
        else -> "Error"
    }
}

@Composable
fun Activity.rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)
    }
    val windowDpSize = with(LocalDensity.current) {
        windowMetrics.bounds.toComposeRect().size.toDpSize()
    }
    val widthWindowSizeClass = when {
        windowDpSize.width < 600.dp -> COMPACT
        windowDpSize.width < 840.dp -> MEDIUM
        else -> EXPANDED
    }
    val heightWindowSizeClass = when {
        windowDpSize.height < 480.dp -> COMPACT
        windowDpSize.height < 900.dp -> MEDIUM
        else -> EXPANDED
    }
    var windowSizeClass = widthWindowSizeClass
    if (widthWindowSizeClass == MEDIUM && heightWindowSizeClass == COMPACT) {
        windowSizeClass = EXPANDED
    }
    return windowSizeClass
    // Use widthWindowSizeClass and heightWindowSizeClass
}
