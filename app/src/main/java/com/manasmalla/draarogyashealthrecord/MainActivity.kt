package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manasmalla.draarogyashealthrecord.ui.HealthRecordApp
import com.manasmalla.draarogyashealthrecord.ui.screens.HealthRecordDestinations
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.home.HomeViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            DrAarogyasHealthRecordTheme {
                /*
                    *userViewModel houses all of the UI Logic related to the user workflow
                    * homeViewModel houses all of the UI Logic for home screen
                */
                val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
                val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
                val isFirstRuntime by userViewModel.isFirstRuntime.collectAsState()
                HealthRecordApp(
                    userViewModel = userViewModel,
                    homeViewModel = homeViewModel,
                    startDestination = if (isFirstRuntime) HealthRecordDestinations.SplashDestination.toString() else HealthRecordDestinations.HomeDestination.toString()
                )
            }
        }
    }
}
