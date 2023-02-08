package com.manasmalla.draarogyashealthrecord.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.unit
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordUiState
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.util.splitCamelCase
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RecordBottomSheetScaffold(
    modifier: Modifier = Modifier,
    recordUiState: RecordUiState = RecordUiState(),
    onUiStateChanged: (RecordUiState) -> Unit = {},
    onAddRecord: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(sheetContent = {
        AddRecordBottomSheet(modifier = modifier,
            measurableMetrics = recordUiState.measurableMetrics,
            measurements = recordUiState.measurements,
            addRecordEnabled = recordUiState.actionsEnabled,
            onUiStateChanged = { onUiStateChanged(recordUiState.copy(measurements = it)) },
            onAddRecord = {
                coroutineScope.launch {
                    state.hide()
                    onAddRecord()
                }
            })
    }, content = {
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        state.show()
                    }
                }, containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Record")
            }
        }) {

            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                content()
            }
        }

    }, sheetState = state, sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordBottomSheet(
    modifier: Modifier = Modifier,
    measurableMetrics: List<Metrics> = listOf(),
    measurements: List<String> = listOf(),
    addRecordEnabled: Boolean = false,
    onUiStateChanged: (List<String>) -> Unit = {},
    onAddRecord: () -> Unit = {}
) {
    Column(modifier = modifier.padding(24.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Add a record", style = MaterialTheme.typography.titleMedium)
        Text(text = "Here’s a quick overview of all of your records")
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(measurableMetrics) { index, metric ->
                OutlinedTextField(value = measurements[index],
                    onValueChange = {
                        val list = measurements.toMutableList()
                        list[index] = it
                        onUiStateChanged(list)
                    },
                    label = {
                        Text(text = metric.name.splitCamelCase, maxLines = 1)
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == measurableMetrics.lastIndex) ImeAction.Done else ImeAction.Next
                    ),
                    trailingIcon = {

                        Text(text = metric.unit)

                    })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAddRecord, modifier = Modifier.fillMaxWidth(), enabled = addRecordEnabled
        ) {
            Text(text = "Add Record")
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
            ), measurements = listOf(
                61.2, 1460.0, 44.0, 76.0, 4.5, 3.2, 18.6
            ).map { it.toString() }, addRecordEnabled = true
        )
    }
}