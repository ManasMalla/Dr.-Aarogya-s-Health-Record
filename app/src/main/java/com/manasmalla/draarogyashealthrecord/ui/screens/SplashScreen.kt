package com.manasmalla.draarogyashealthrecord.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.GoogleSansFontFamily


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier, isFirstRuntime: Boolean = true, onGetStarted: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DrAarogyaHealthRecordAppLogo()

        Spacer(modifier = Modifier.height(64.dp))

        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxHeight()) {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                content = {})
            Column(
                Modifier
                    .padding(32.dp)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor_aarogya),
                    contentDescription = "Dr. Aarogya",
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 27.dp),
                    contentScale = ContentScale.FillHeight
                )

                AnimatedVisibility(visible = isFirstRuntime) {
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 27.dp)
                    ) {
                        Text(text = "Get Started", fontFamily = GoogleSansFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
fun DrAarogyaHealthRecordAppLogo() {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ) {
        Text(
            text = "Dr. Aarogya's",
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Health Record",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayMedium
        )
        Row {
            Text(
                text = "Developed by ",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp
            )
            Text(
                text = "Manas Malla",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            SplashScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreen_NotFirstTimePreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {

        SplashScreen(isFirstRuntime = false)

    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenDynamicPreview() {
    DrAarogyasHealthRecordTheme {
        SplashScreen()
    }
}

