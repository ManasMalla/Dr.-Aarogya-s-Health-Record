package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.unit
import com.manasmalla.draarogyashealthrecord.util.splitCamelCase


@Composable
fun MetricCard(metric: Metrics, value: Double) {
    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Image(
            imageVector = if (metric == Metrics.Weight) Icons.Rounded.Scale else if (metric == Metrics.BloodPressure || metric == Metrics.BloodSugar) Icons.Rounded.MonitorHeart else Icons.Rounded.QueryStats,
            contentDescription = null
        )
        Column(modifier = Modifier.wrapContentHeight()) {
            Text(text = metric.name.splitCamelCase, style = MaterialTheme.typography.labelLarge)
            Text(text = "${value}${metric.unit}")
        }
    }
}
