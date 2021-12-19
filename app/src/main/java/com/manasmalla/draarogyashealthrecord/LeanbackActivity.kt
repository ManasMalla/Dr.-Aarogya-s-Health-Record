package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.manasmalla.draarogyashealthrecord.landing.LandingScreenExpanded
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme

class LeanbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrAarogyasHealthRecordTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        LandingScreenExpanded(onFinishedListener = {})
    }
}

@Preview(showBackground = true, widthDp = 1920, heightDp = 1080)
@Composable
fun DefaultPreview() {
    DrAarogyasHealthRecordTheme (dynamicColor = false){
        Greeting()
    }
}