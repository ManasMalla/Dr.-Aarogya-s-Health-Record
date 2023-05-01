package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manasmalla.draarogyashealthrecord.ui.HealthRecordApp
import com.manasmalla.draarogyashealthrecord.ui.screens.HealthRecordDestinations
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.home.HomeViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WindowCompat.setDecorFitsSystemWindows(window, false)

            Column {

                DrAarogyasHealthRecordTheme {
                    /*
                        *userViewModel houses all of the UI Logic related to the user workflow
                        * homeViewModel houses all of the UI Logic for home screen
                    */
                    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
                    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

                    val recordViewModel: RecordViewModel =
                        viewModel(factory = RecordViewModel.Factory)
                    val isFirstRuntime by remember {
                        derivedStateOf {
                            userViewModel.isFirstRuntime.value
                        }
                    }

                    HealthRecordApp(
                        isFirstRuntime = isFirstRuntime ?: true,
                        userViewModel = userViewModel,
                        homeViewModel = homeViewModel,
                        onThemeToggle = {
                            //Theme updating logic
//                            when(themeViewModel.isDark){
//                                true -> themeViewModel.updateLightTheme()
//                                false -> themeViewModel.updateDarkTheme()
//                            }
                        },
                        recordViewModel = recordViewModel,
                        startDestination = if (isFirstRuntime != false) HealthRecordDestinations.SplashDestination.toString()
                        else HealthRecordDestinations.HomeDestination.toString()
                    )
                }
            }
        }
    }
}
