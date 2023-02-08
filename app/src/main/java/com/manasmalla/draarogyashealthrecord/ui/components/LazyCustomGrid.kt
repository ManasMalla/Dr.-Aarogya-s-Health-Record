package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun LazyCustomGrid(modifier: Modifier = Modifier, items: @Composable () -> Unit) {
    Layout(content = items, modifier = modifier) { measurables, constraints ->
        var rowWidth = 0
        val positionY = mutableListOf<Int>()
        val positionX = mutableListOf<Int>()
        var columnHeight = 0
        val placeables = measurables.mapIndexed { index, it ->
            val placeable = it.measure(constraints)
            if (rowWidth + placeable.width >= constraints.maxWidth) {
                rowWidth = placeable.width
                columnHeight += placeable.height
            } else {
                rowWidth += placeable.width + if (index == 0) 0 else (8.dp.toPx().roundToInt())
            }
            positionX.add(rowWidth - placeable.width)
            positionY.add(columnHeight)
            placeable
        }
        layout(constraints.maxWidth, positionY.last() + placeables.last().height) {
            for (index in 0 until placeables.size) {
                placeables[index].placeRelative(positionX[index], positionY[index])
            }
        }
    }
}