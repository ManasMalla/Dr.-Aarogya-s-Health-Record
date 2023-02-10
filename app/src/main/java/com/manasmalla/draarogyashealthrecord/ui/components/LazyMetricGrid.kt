package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout


@Composable
fun LazyMetricGrid(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->

        var rowWidth = 0
        var rowHeight = 0
        var composableWidth = 0
        var composableHeight = 0

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            if (rowWidth + placeable.width < constraints.maxWidth) {
                //We have room in the row
                rowWidth += placeable.width
                rowHeight = maxOf(rowHeight, placeable.height)
            } else {
                //We need to move to the next row
                composableHeight += rowHeight
                composableWidth = maxOf(composableWidth, rowWidth)
                rowWidth = placeable.width
                rowHeight = placeable.height
            }
            placeable
        }
        layout(
            composableWidth,
            composableHeight + placeables.last().height
        ) {
            var placeableHeight = 0
            var positionX = 0
            var positionY = 0
            placeables.forEachIndexed { index, placeable ->
                if (positionX + placeable.width < constraints.maxWidth) {
                    placeable.placeRelative(positionX, positionY)
                    //We have room in the row
                    positionX += placeable.width
                    rowHeight = maxOf(rowHeight, placeable.height)
                } else {
                    //We need to move to the next row
                    positionY += rowHeight
                    positionX = 0
                    placeable.placeRelative(positionX, positionY)
                    positionX = placeable.width
                    rowHeight = placeable.height
                }
            }
        }

    }
}
