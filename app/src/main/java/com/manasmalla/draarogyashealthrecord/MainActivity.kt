package com.manasmalla.draarogyashealthrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manasmalla.draarogyashealthrecord.ui.HealthRecordApp
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
                HealthRecordApp(userViewModel = userViewModel, homeViewModel = homeViewModel)
            }
        }
    }
}
