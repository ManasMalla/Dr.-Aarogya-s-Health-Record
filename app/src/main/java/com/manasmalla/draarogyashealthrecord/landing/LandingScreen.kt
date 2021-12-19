package com.manasmalla.draarogyashealthrecord.landing

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LiveData
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.components.AnimatedLoadingBar
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.WindowSizeClass
import com.manasmalla.draarogyashealthrecord.ui.theme.WindowSizeClass.*
import com.manasmalla.draarogyashealthrecord.ui.theme.stringValue
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun LandingScreen(windowSizeData:LiveData<WindowSizeClass?>, onFinishedListener: (Float) -> Unit) {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        val windowSize by windowSizeData.observeAsState()
        Log.d("WS", windowSize.stringValue())
        when (windowSize) {
            COMPACT -> LandingScreenCompact(onFinishedListener)
            MEDIUM -> LandingScreenMedium(onFinishedListener)
            EXPANDED -> LandingScreenExpanded(onFinishedListener)
            null -> LandingScreenExpanded(onFinishedListener)
        }
    }
}

@Composable
fun LandingScreenCompact(onFinishedListener: (Float) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        HospitalBackground()
        TitleBar(
            onFinishedListener = onFinishedListener,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        )
        Image(
            painter = painterResource(id = R.drawable.doctor_aarogya),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 10.dp)
        )
    }
}

@Composable
fun LandingScreenMedium(onFinishedListener: (Float) -> Unit) {
    HospitalBackground()
    ConstraintLayout {
        val (aarogya, titleBar) = createRefs()
        val titleBarGuide = createGuidelineFromStart(0.42f)
        val halfScreen = createGuidelineFromStart(0.55f)
        Image(
            painter = painterResource(id = R.drawable.doctor_aarogya),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.constrainAs(aarogya) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(halfScreen)
                width = Dimension.fillToConstraints
            }
        )
        TitleBar(
            onFinishedListener = onFinishedListener,
            progressBarWidth = 1f,
            barHeight = 60.dp,
            modifier = Modifier
                .constrainAs(titleBar) {
                    start.linkTo(titleBarGuide)
                    top.linkTo(parent.top, margin = 32.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        )

    }
}

@Composable
fun LandingScreenExpanded(onFinishedListener: (Float) -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        HospitalBackground()
        ConstraintLayout {
            val (aarogya, titleBar) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.doctor_aarogya),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.constrainAs(aarogya) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 128.dp)
                    top.linkTo(parent.top, margin = 32.dp)
                    height = Dimension.fillToConstraints
                }
            )
            TitleBar(
                onFinishedListener = onFinishedListener,
                progressBarWidth = 1f,
                barHeight = 80.dp,
                modifier = Modifier
                    .constrainAs(titleBar) {
                        start.linkTo(aarogya.end, margin = 32.dp)
                        top.linkTo(parent.top, margin = 32.dp)
                        end.linkTo(parent.end, margin = 32.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                subTitleStyle = MaterialTheme.typography.displaySmall.copy(fontSize = 70.sp, lineHeight = 50.sp),
                titleStyle = MaterialTheme.typography.displayLarge.copy(fontSize = 100.sp, lineHeight = 80.sp)
            )

        }
    }
}

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.displayLarge,
    subTitleStyle: TextStyle = MaterialTheme.typography.displaySmall,
    barHeight: Dp = 42.dp,
    progressBarWidth: Float = 0.8f,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onFinishedListener: (Float) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Column() {
            Text(
                "Dr. Aarogya's",
                style = subTitleStyle,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Health Record",
                style = titleStyle,
                color = MaterialTheme.colorScheme.primaryContainer,
            )
        }
        Spacer(Modifier.height(32.dp))
        AnimatedLoadingBar(
            modifier = Modifier
                .height(barHeight)
                .fillMaxWidth(progressBarWidth), onFinishedListener = onFinishedListener
        )
    }
}

@Composable
fun HospitalBackground() {
    Image(
        painter = painterResource(id = R.drawable.hospital_background),
        contentDescription = null, contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize().background(Color.White),
        alignment = Alignment.BottomStart
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Landing Screen • Compact")
@Composable
fun LandingScreenPreviewCompact() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LandingScreenCompact(onFinishedListener = {})
    }
}

@Preview(
    showBackground = true,
    widthDp = 673,
    heightDp = 841,
    name = "Landing Screen • Foldable Portrait"
)
@Composable
fun LandingScreenPreviewMedium() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LandingScreenMedium(onFinishedListener = {})
    }
}

@Preview(
    showBackground = true,
    widthDp = 841,
    heightDp = 673,
    name = "Landing Screen • Foldable Landscape"
)
@Composable
fun LandingScreenPreviewMediumL() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LandingScreenExpanded(onFinishedListener = {})
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Landing Screen • Expanded")
@Composable
fun LandingScreenPreviewExpanded() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LandingScreenExpanded(onFinishedListener = {})
    }
}
