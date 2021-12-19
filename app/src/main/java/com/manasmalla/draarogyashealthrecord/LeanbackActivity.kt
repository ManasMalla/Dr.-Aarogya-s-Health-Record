package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.manasmalla.draarogyashealthrecord.landing.LandingScreenExpanded
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModel
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModelFactory
import com.manasmalla.draarogyashealthrecord.ui.HealthRecordNavHost
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.WindowSizeClass

class LeanbackActivity : ComponentActivity() {
    val viewModel:HealthRecordViewModel by viewModels {
        HealthRecordViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.setWindowSizeClass(WindowSizeClass.EXPANDED)
            DrAarogyasHealthRecordTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HealthRecordNavHost(navController = rememberNavController(), viewModel = viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1920, heightDp = 1080)
@Composable
fun DefaultPreview() {
    DrAarogyasHealthRecordTheme (dynamicColor = false){
        LandingScreenExpanded(onFinishedListener = {})
    }
}