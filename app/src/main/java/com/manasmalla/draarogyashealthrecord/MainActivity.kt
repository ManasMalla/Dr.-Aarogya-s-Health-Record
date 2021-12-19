package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.manasmalla.draarogyashealthrecord.landing.LandingScreen
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModel
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModelFactory
import com.manasmalla.draarogyashealthrecord.ui.HealthRecordNavHost
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme

class MainActivity : ComponentActivity() {
    private val viewModel: HealthRecordViewModel by viewModels {
        HealthRecordViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrAarogyasHealthRecordTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Replace With NavHost
                    HealthRecordNavHost(navController = rememberNavController(), viewModel = viewModel)
                }
            }
        }
    }
}
