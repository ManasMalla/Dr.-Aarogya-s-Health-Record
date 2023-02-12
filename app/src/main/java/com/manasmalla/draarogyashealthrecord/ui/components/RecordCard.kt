package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.util.DateConverter
import java.util.Calendar
import java.util.Date

@Composable
fun RecordCard(modifier: Modifier = Modifier, record: Map<Metrics, Double>, date: Date) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = DateConverter().dateFormatters[0].format(date),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.widthIn(min = 54.dp)
                    )
                    Text(
                        text = DateConverter().dateFormatters[1].format(date),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = DateConverter().dateFormatters[2].format(date),
                    )
                }
                LazyMetricGrid {
                    record.entries.sortedByDescending { it.key.name.length }
                        .take(if (record.entries.sortedByDescending { it.key.name.length }.size > 1) 2 else 1)
                        .map { metric ->
                            MetricCard(metric = metric.key, value = metric.value)
                        }
                }
            }
            AnimatedVisibility(visible = record.entries.sortedByDescending { it.key.name.length }.size > 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    LazyMetricGrid {
                        record.entries.sortedByDescending { it.key.name.length }.drop(2)
                            .map { metric ->
                                MetricCard(metric = metric.key, value = metric.value)
                            }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LazyMetricGridPreview() {
    DrAarogyasHealthRecordTheme {
        RecordCard(
            record = mapOf(
                Metrics.Calories to 1650.0,
                Metrics.BMI to 20.0,
                Metrics.BloodPressure to 120.0,
                //Metrics.BloodSugar to 120.0,
            ), date = Calendar.getInstance().time
        )
    }
}