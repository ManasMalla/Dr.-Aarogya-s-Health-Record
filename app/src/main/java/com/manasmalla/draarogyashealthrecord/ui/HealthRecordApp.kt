package com.manasmalla.draarogyashealthrecord.ui

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manasmalla.draarogyashealthrecord.ui.screens.HealthRecordDestinations.*
import com.manasmalla.draarogyashealthrecord.ui.screens.HomeScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.HomeViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.LoginScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.SplashScreen
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordApp(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(modifier = modifier.fillMaxSize()) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .windowInsetsPadding(WindowInsets.statusBars),
            color = MaterialTheme.colorScheme.background
        ) {

            NavHost(
                navController = navController,
                startDestination = HomeDestination.toString(),
            ) {

                composable(SplashDestination.toString()) {
                    //TODO: Check if first runtime by checking number of users in room database
                    // else navigate to home screen in 2 seconds
                    var isFirstRuntime by remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(true) {
                        delay(2000)
                        isFirstRuntime = !isFirstRuntime
                    }
                    SplashScreen(isFirstRuntime = isFirstRuntime, onGetStarted = {
                        navController.navigate(LoginDestination.toString())
                    })
                }
                composable(LoginDestination.toString()) {
                    LoginScreen(onRegisterUser = {
                        navController.navigate(HomeDestination.toString())
                    })
                }

                composable(HomeDestination.toString()) {

                    val viewModel:HomeViewModel = viewModel()
                    LaunchedEffect(true){
                        viewModel.getRecords()
                    }
                    HomeScreen(homeUiState = viewModel.uiState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HealthRecordAppPreview() {
    DrAarogyasHealthRecordTheme {
        HealthRecordApp()
    }
}