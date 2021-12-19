package com.manasmalla.draarogyashealthrecord.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import kotlinx.coroutines.delay

@Composable
fun AnimatedProgressBar(
    modifier: Modifier = Modifier,
    size: Float = 0f,
    onFinishedListener: (Float) -> Unit = {}
) {
    val progressBarWidth by animateFloatAsState(
        size,
        animationSpec = tween(durationMillis = 3000),
        finishedListener = onFinishedListener
    )
    Surface(
        shape = RoundedCornerShape(50.dp), modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = progressBarWidth < 0.1f) {
                    Text(
                        stringResource(R.string.please_wait),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressBarWidth)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = progressBarWidth >= 0.1f, enter = slideInHorizontally()) {
                    Text(
                        stringResource(R.string.loading),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedLoadingBar(modifier: Modifier = Modifier, onFinishedListener: (Float) -> Unit) {
    var state by remember {
        mutableStateOf(0f)
    }
    LaunchedEffect(key1 = Unit) {
        delay(500)
        state = 1f
    }
    AnimatedProgressBar(modifier = modifier, size = state, onFinishedListener = onFinishedListener)
}


@Preview(showBackground = true, widthDp = 450, heightDp = 120, name = "Animated Progress Bar")
@Composable
fun AnimatedProgressBarPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        var size by remember {
            mutableStateOf(0f)
        }
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                Modifier
                    .height(40.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { size = 0.09f }) {
                    Text(text = "10%")
                }
                Button(onClick = { size = 0.4f }) {
                    Text(text = "40%")
                }
                Button(onClick = { size = 0.6f }) {
                    Text(text = "60%")
                }
                Button(onClick = { size = 0.8f }) {
                    Text(text = "80%")
                }
                Button(onClick = { size = 1f }) {
                    Text(text = "100%")
                }
            }
            AnimatedProgressBar(size = size, modifier = Modifier.height(50.dp), onFinishedListener = {})
        }
    }
}

@Preview(showBackground = true, heightDp = 54, widthDp = 350, name = "Animated Loading Bar")
@Composable
fun AnimatedLoadingBarPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        AnimatedLoadingBar(onFinishedListener = {})
    }
}