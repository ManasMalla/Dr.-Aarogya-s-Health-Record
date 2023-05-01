package com.manasmalla.draarogyashealthrecord.ui.screens.record

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.Record
import com.manasmalla.draarogyashealthrecord.model.unit
import com.manasmalla.draarogyashealthrecord.ui.components.VerticalGrid
import com.manasmalla.draarogyashealthrecord.ui.screens.home.ErrorScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.home.LoadingScreen
import com.manasmalla.draarogyashealthrecord.util.DateConverter
import com.manasmalla.draarogyashealthrecord.util.splitCamelCase
import kotlin.math.ceil

@Composable
fun ManageRecordScreen(
    uiState: RecordUiState = RecordUiState.Loading,
    onEditRecord: () -> Unit = {},
    onShareRecord: () -> Unit = {},
    onUpdateRecord: () -> Unit = {},
    onDeleteRecord: () -> Unit = {},
    pastRecordMetrics: List<Record> = listOf(),
    onUiStateChanged: (RecordUiState.Success) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    when (uiState) {
        RecordUiState.Error -> ErrorScreen()
        RecordUiState.Loading -> LoadingScreen()
        is RecordUiState.Success -> ManageRecordScaffold(uiState = uiState,
            onEditRecord = onEditRecord,
            onDeleteRecord = onDeleteRecord,
            onNavigateBack = onNavigateBack,
            onUpdateRecord = onUpdateRecord,
            onShareRecord = onShareRecord,
            pastRecordMetrics = pastRecordMetrics,
            onUiStateChanged = {
                onUiStateChanged(
                    uiState.copy(
                        measurements = it
                    )
                )
            },
            onCancelEdit = {
                onUiStateChanged(uiState.copy(actionsEnabled = false))
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ManageRecordScaffold(
    uiState: RecordUiState.Success = RecordUiState.Success(actionsEnabled = true),
    onEditRecord: () -> Unit = {},
    onShareRecord: () -> Unit = {},
    onUpdateRecord: () -> Unit = {},
    onDeleteRecord: () -> Unit = {},
    pastRecordMetrics: List<Record> = listOf(),
    onUiStateChanged: (List<String>) -> Unit = {},
    onCancelEdit: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(floatingActionButton = {
        AnimatedVisibility(visible = !uiState.actionsEnabled) {
            FloatingActionButton(onClick = onEditRecord) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit Record")
            }
        }
    }, topBar = {
        LargeTopAppBar(title = {
            Text(text = "Report Analysis")
        }, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Rounded.ChevronLeft, contentDescription = null)
            }
        }, actions = {
            IconButton(onClick = onShareRecord) {
                Icon(imageVector = Icons.Rounded.IosShare, contentDescription = null)
            }
            IconButton(onClick = onDeleteRecord) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                )
            }
        })
    }) {

        var isDropDownOpen by remember {
            mutableStateOf(false)
        }

        var selectedMetricItem by remember {
            mutableStateOf("Metric")
        }

        val focusManager = LocalFocusManager.current

        LaunchedEffect(key1 = uiState.actionsEnabled) {
            focusManager.clearFocus()
        }

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row {
                    Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                        Text(
                            text = "Analyze your health metrics to make your life more fit.",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Get started by choosing a metric from the dropdown",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = DateConverter().dateFormatters[0].format(uiState.date),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.widthIn(min = 54.dp)
                        )
                        Text(
                            text = DateConverter().dateFormatters[1].format(uiState.date),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = DateConverter().dateFormatters[2].format(uiState.date),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                VerticalGrid {
                    uiState.measurableMetrics.forEachIndexed { index, metric ->
                        OutlinedTextField(
                            value = uiState.measurements[index],
                            onValueChange = {
                                val list = uiState.measurements.toMutableList()
                                list[index] = it
                                onUiStateChanged(list)
                            },
                            label = {
                                Text(
                                    text = metric.name.splitCamelCase,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                end = if (index % 2 == 0) 16.dp else 0.dp
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = if (index == uiState.measurableMetrics.lastIndex) ImeAction.Done else ImeAction.Next
                            ),
                            trailingIcon = if (uiState.measurements[index].isNotBlank()) trailingMetricIcon(
                                metric = metric.unit,
                                visible = uiState.measurements[index].isNotBlank()
                            ) else null,
                            readOnly = !uiState.actionsEnabled
                        )
                    }
                }
                AnimatedVisibility(visible = !uiState.actionsEnabled && pastRecordMetrics.size > 1) {
                    Column {
                        Box {
                            FilterChip(
                                selected = isDropDownOpen,
                                onClick = { isDropDownOpen = !isDropDownOpen },
                                label = {
                                    Text(text = selectedMetricItem)
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (isDropDownOpen) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                        contentDescription = null
                                    )
                                })

                            DropdownMenu(
                                expanded = isDropDownOpen,
                                onDismissRequest = { isDropDownOpen = false }) {
                                uiState.measurableMetrics.forEach { metric ->
                                    DropdownMenuItem(text = { Text(text = metric.name.splitCamelCase) },
                                        onClick = {
                                            selectedMetricItem = metric.name.splitCamelCase
                                            isDropDownOpen = false
                                        })
                                }
                            }
                        }
                        MetricGraph(metrics = if (selectedMetricItem != "Metric") pastRecordMetrics.map { metric ->
                            metric.record[Metrics.valueOf(
                                selectedMetricItem.replace(" ", "")
                            )] ?: 0.0
                        }.filter { metric -> metric != 0.0 } else listOf(),
                            if (selectedMetricItem != "Metric") uiState.measurableMetrics.zip(
                                uiState.measurements
                            )
                                .toMap()[Metrics.valueOf(
                                selectedMetricItem.replace(" ", "")
                            )]?.toDoubleOrNull() ?: 0.0 else 0.0)

                    }
                }

                AnimatedVisibility(visible = uiState.actionsEnabled) {
                    Column {
                        Button(
                            onClick = onUpdateRecord,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp)
                        ) {
                            Text(text = "Update Record")
                        }
                        OutlinedButton(
                            onClick = onCancelEdit, modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Cancel")
                        }
                    }
                }
//            Button(
//                onClick = onDeleteRecord,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer
//                )
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Delete,
//                    contentDescription = null,
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(text = "Delete Record")
//            }
            }
        }
    }
}

@Composable
fun MetricGraph(metrics: List<Double> = listOf(), currentRecordMetric: Double) {
    AnimatedVisibility(visible = metrics.isNotEmpty() && metrics.size > 1) {
        Row {
            Column(
                verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .height(156.dp)
                    .padding(vertical = 28.dp)
            ) {
                Text(text = ceil(metrics.max()).toInt().toString())
                Text(text = "0")
            }
            Column(modifier = Modifier.padding(vertical = 24.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    metrics.take(6).reversed().forEach {
                        Surface(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight((it / metrics.max()).toFloat()),
                            color = if (it / metrics.max() > 0.5) (if (currentRecordMetric != it) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary) else (if (currentRecordMetric != it) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.error)
                        ) {

                        }
                    }
                }
                Divider()
                Text(
                    text = "Metric over Dates",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
