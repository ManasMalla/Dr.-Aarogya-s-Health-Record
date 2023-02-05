package com.manasmalla.draarogyashealthrecord.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.GoogleSansFontFamily
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onRegisterUser: () -> Unit = {}
) {
    val userViewModel: UserViewModel = viewModel()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .animateContentSize()
            .verticalScroll(state = scrollState),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .padding(bottom = 48.dp, top = 64.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .size(160.dp),
            shape = MaterialYouClipper()
        ) {
            Icon(
                Icons.Rounded.CameraAlt,
                contentDescription = "Add an image",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                modifier = Modifier.padding(56.dp)
            )
        }
        Text(text = "About You", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam nec erat bibendum, tempus erat at, suscipit urna. ",
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 24.dp)
        )
        Row {
            OutlinedTextField(
                value = userViewModel.uiState.name,
                onValueChange = userViewModel::updateName,
                label = {
                    Text(text = "Name")
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = userViewModel.uiState.formattedAge,
                onValueChange = userViewModel::updateAge,
                label = {
                    Text(text = "Age")
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(96.dp), singleLine = true, keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Gender")
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            RadioButton(selected = userViewModel.uiState.gender == Gender.Male,
                onClick = { userViewModel.updateGender(Gender.Male) })
            Text(text = Gender.Male.name)
            Spacer(modifier = modifier.weight(1f))
            RadioButton(selected = userViewModel.uiState.gender == Gender.Female,
                onClick = { userViewModel.updateGender(Gender.Female) })
            Text(text = Gender.Female.name)
            Spacer(modifier = modifier.weight(1f))
            RadioButton(selected = userViewModel.uiState.gender == Gender.Other,
                onClick = { userViewModel.updateGender(Gender.Other) })
            Text(text = Gender.Other.name)
        }
        Divider(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp, start = 8.dp, end = 8.dp))
        Text(text = "Lorem Ipsum", fontWeight = FontWeight.Medium)
        Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing.")
        Spacer(modifier = Modifier.height(8.dp))
        LazyCustomGrid {
            Metrics.values().map { metric ->
                InputChip(
                    selected = userViewModel.uiState.metric.contains(metric),
                    onClick = { userViewModel.updateMetric(metric) },
                    label = {
                        Text(text = metric.name.splitCamelCase)
                    }
                )
            }
        }
        AnimatedVisibility(
            visible = userViewModel.uiState.metric.contains(Metrics.Weight) || userViewModel.uiState.metric.contains(
                Metrics.Height
            )
        ) {
            Text(
                text = "Select the respective units",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnimatedVisibility(visible = userViewModel.uiState.metric.contains(Metrics.Weight)) {
                Column {
                    Text(text = "Weight")
                    Spacer(modifier = Modifier.height(4.dp))
                    SegmentedControl(
                        items = listOf("kgs", "lbs"),
                        selectedItemIndex = userViewModel.uiState.weightUnit,
                        onItemSelection = userViewModel::updateWeightUnit)
                }
            }
            AnimatedVisibility(visible = userViewModel.uiState.metric.contains(Metrics.Height)) {
                Column {
                    Text(text = "Height")
                    Spacer(modifier = Modifier.height(4.dp))
                    SegmentedControl(
                        items = listOf("cms", "feet", "in"), selectedItemIndex = userViewModel.uiState.heightUnit,
                        onItemSelection = userViewModel::updateHeightUnit)
                }
            }
        }
        Button(
            onClick = onRegisterUser, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(text = "Get Started", fontFamily = GoogleSansFontFamily)
        }
    }
}

val String.splitCamelCase: String
    get() {
        return this.replace(
            String.format(
                "%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"
            ).toRegex(),
            " "
        )
    }


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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LoginScreen()
    }
}