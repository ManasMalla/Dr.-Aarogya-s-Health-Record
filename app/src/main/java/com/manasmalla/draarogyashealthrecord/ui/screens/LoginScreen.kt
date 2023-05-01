package com.manasmalla.draarogyashealthrecord.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.heightUnits
import com.manasmalla.draarogyashealthrecord.model.weightUnits
import com.manasmalla.draarogyashealthrecord.ui.ProfileUiState
import com.manasmalla.draarogyashealthrecord.ui.components.LazyCustomGrid
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.GoogleSansFontFamily
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper
import com.manasmalla.draarogyashealthrecord.util.splitCamelCase

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState = ProfileUiState.Loading,
    userUiState: UserUiState = UserUiState(
        name = "", age = "", gender = Gender.Male
    ),
    primaryActionLabel: String = "Get Started",
    hasSecondaryActions: Boolean = false,
    secondaryActions: @Composable () -> Unit = {},
    updateUiState: (UserUiState) -> Unit = {},
    onRegisterUser: (Uri?) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.systemBars)
            .animateContentSize()
            .verticalScroll(state = scrollState),
    ) {
        var imageUri: Uri? by remember {
            mutableStateOf(null)
        }
        ProfilePictureContainer(imageUri = imageUri, onSetImage = {
            imageUri = it
        }, profileUiState = profileUiState)
        Text(text = "About You", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The information you will provide will help us know you better and tailor the app experience just for you! ",
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 24.dp)
        )
        NameAndAgeTextFields(
            userUiState = userUiState, onUpdateUiState = updateUiState
        )
        Spacer(modifier = Modifier.height(12.dp))
        GenderRadioList(
            gender = userUiState.gender,
            updateGender = {
                updateUiState(userUiState.copy(gender = it))
            }
        )
        Divider(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp, start = 8.dp, end = 8.dp))
        MetricChooserGrid(metrics = userUiState.metric, onMetricsChanged = {
            updateUiState(userUiState.copy(metric = it))
        })
        UnitPickerCard(
            doesContainWeightMetric = userUiState.metric.contains(Metrics.Weight),
            doesContainHeightMetric = userUiState.metric.contains(Metrics.Height),
            weightMetricIndex = userUiState.weightUnit,
            heightMetricIndex = userUiState.heightUnit,
            onWeightMetricChanged = {
                Log.d("UpdateWeightMetric", it.toString())
                updateUiState(userUiState.copy(weightUnit = it))
            },
            onHeightMetricChanged = {
                updateUiState(userUiState.copy(heightUnit = it))
            }
        )
        Button(
            onClick = { onRegisterUser(imageUri) },
            modifier = if (hasSecondaryActions) Modifier
                .fillMaxWidth()
                .padding(top = 24.dp) else Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            enabled = userUiState.isValid
        ) {
            Text(text = primaryActionLabel, fontFamily = GoogleSansFontFamily)
        }
        secondaryActions()
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoilApi::class)
@Preview
@Composable
fun ProfilePictureContainer(
    imageUri: Uri? = null,
    profileUiState: ProfileUiState = ProfileUiState.Loading,
    onSetImage: (Uri?) -> Unit = {}
) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            onSetImage(it)
        }
    AnimatedContent(targetState = imageUri != null) {
        when (it) {
            true -> {
                Image(painter = rememberImagePainter(imageUri), contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 48.dp, top = 64.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .size(160.dp)
                        .clickable {
                            //Get profile picture
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        .clip(MaterialYouClipper()), contentScale = ContentScale.Crop
                )
            }

            else -> {
                when (profileUiState) {
                    is ProfileUiState.Storage -> {
                        Image(
                            bitmap = profileUiState.bitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(bottom = 48.dp, top = 64.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .size(160.dp)
                                .clickable {
                                    //Get profile picture
                                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }
                                .clip(MaterialYouClipper()),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .padding(bottom = 48.dp, top = 64.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .size(160.dp)
                                .clickable {
                                    //Get profile picture
                                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                },
                            shape = MaterialYouClipper()
                        ) {
                            Icon(
                                Icons.Rounded.CameraAlt,
                                contentDescription = "Add an image",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                modifier = Modifier.padding(56.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun UnitPickerCard(
    doesContainWeightMetric: Boolean = true,
    weightMetricIndex: Int = 0,
    onWeightMetricChanged: (Int) -> Unit = {},
    doesContainHeightMetric: Boolean = true,
    heightMetricIndex: Int = 0,
    onHeightMetricChanged: (Int) -> Unit = {}
) {
    AnimatedVisibility(
        visible = doesContainHeightMetric.or(doesContainWeightMetric)
    ) {
        Column {
            Text(
                text = "Select the respective units",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AnimatedVisibility(visible = doesContainWeightMetric) {
                    Column {
                        Text(text = "Weight")
                        Spacer(modifier = Modifier.height(4.dp))
                        SegmentedControl(
                            items = weightUnits,
                            selectedItemIndex = weightMetricIndex,
                            onItemSelection = onWeightMetricChanged
                        )
                    }
                }
                AnimatedVisibility(visible = doesContainHeightMetric && !doesContainWeightMetric) {
                    Column {
                        Text(text = "Height")
                        Spacer(modifier = Modifier.height(4.dp))
                        SegmentedControl(
                            items = heightUnits,
                            selectedItemIndex = heightMetricIndex,
                            onItemSelection = onHeightMetricChanged
                        )
                    }
                }
            }
            AnimatedVisibility(visible = doesContainHeightMetric && doesContainWeightMetric) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Height")
                    Spacer(modifier = Modifier.height(4.dp))
                    SegmentedControl(
                        items = heightUnits,
                        selectedItemIndex = heightMetricIndex,
                        onItemSelection = onHeightMetricChanged
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MetricChooserGrid(
    metrics: List<Metrics> = listOf(),
    onMetricsChanged: (List<Metrics>) -> Unit = {}
) {
    Column {
        Text(text = "Metrics", fontWeight = FontWeight.Medium)
        Text(text = "Select the metrics that you wish to record")
        Spacer(modifier = Modifier.height(8.dp))
        LazyCustomGrid {
            Metrics.values().map { metric ->
                InputChip(
                    selected = metrics.contains(metric),
                    onClick = {
                        val updatedMetricList = metrics.toMutableList()
                        if (updatedMetricList.contains(metric)) {
                            updatedMetricList.remove(metric)
                        } else {
                            updatedMetricList.add(metric)
                        }
                        onMetricsChanged(updatedMetricList)
                    },
                    label = {
                        Text(text = metric.name.splitCamelCase)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NameAndAgeTextFields(
    userUiState: UserUiState = UserUiState("", "", Gender.Male),
    onUpdateUiState: (UserUiState) -> Unit = {}
) {
    Row {

        OutlinedTextField(
            value = userUiState.name,
            onValueChange = {
                onUpdateUiState(userUiState.copy(name = it))
            },
            label = {
                Text(text = "Name")
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = userUiState.age,
            onValueChange = {
                onUpdateUiState(userUiState.copy(age = it))
            },
            label = {
                Text(text = "Age")
            },
            modifier = Modifier
                .padding(start = 16.dp)
                .width(96.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenderRadioList(gender: Gender = Gender.Male, updateGender: (Gender) -> Unit = {}) {
    Column {
        Text(text = "Gender")
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            RadioButton(selected = gender == Gender.Male,
                onClick = { updateGender(Gender.Male) })
            Text(text = Gender.Male.name)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(selected = gender == Gender.Female,
                onClick = { updateGender(Gender.Female) })
            Text(text = Gender.Female.name)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(selected = gender == Gender.Other,
                onClick = { updateGender(Gender.Other) })
            Text(text = Gender.Other.name)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DrAarogyasHealthRecordTheme {
        LoginScreen()
    }
}