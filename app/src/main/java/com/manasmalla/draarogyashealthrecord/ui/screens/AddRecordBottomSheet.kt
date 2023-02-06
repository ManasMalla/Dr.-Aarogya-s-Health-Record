package com.manasmalla.draarogyashealthrecord.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.H
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import kotlin.math.roundToInt

fun Double.formattedMeasurement(metrics: Metrics): String{
    return "${if(this.roundToInt().toDouble() == this) this.roundToInt() else this}${metrics.unit}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordBottomSheet(
    modifier: Modifier = Modifier,
    measurableMetrics: List<Metrics> = listOf(),
    recordUiState: RecordUiState = RecordUiState(),
    onAddRecord: ()->Unit = {}
) {
    ModalDrawerSheet {
        Column(modifier = modifier.padding(24.dp)) {
            Text(text = "Add a record", style = MaterialTheme.typography.titleMedium)
            Text(text = "Here’s a quick overview of all of your records")
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(measurableMetrics) { index, metric ->
                    OutlinedTextField(
                        value = recordUiState.measurements[index].formattedMeasurement(metric),
                        onValueChange = {},
                        label = {
                            Text(text = metric.name.splitCamelCase)
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddRecord, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Add Record")
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun AddRecordBottomSheetPreview() {
    DrAarogyasHealthRecordTheme {
        AddRecordBottomSheet(
            measurableMetrics = listOf(
                Metrics.Weight,
                Metrics.Calories,
                Metrics.FatPercentage,
                Metrics.WaterPercentage,
                Metrics.MuscleMass,
                Metrics.BoneMass,
                Metrics.BMI
            ), recordUiState = RecordUiState(
                measurements =
                listOf(
                    61.2,
                    1460.0,
                    44.0,
                    76.0,
                    4.5,
                    3.2,
                    18.6
                )
            )
        )
    }
}