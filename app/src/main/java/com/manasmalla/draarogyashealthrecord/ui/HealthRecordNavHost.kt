package com.manasmalla.draarogyashealthrecord.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.manasmalla.draarogyashealthrecord.HealthRecordScreens.Landing
import com.manasmalla.draarogyashealthrecord.HealthRecordScreens.Login
import com.manasmalla.draarogyashealthrecord.landing.LandingScreen
import com.manasmalla.draarogyashealthrecord.model.HealthRecordViewModel

@Composable
fun HealthRecordNavHost(navController: NavHostController, viewModel: HealthRecordViewModel) {
    NavHost(navController = navController, startDestination = Landing.name) {
        composable(Landing.name) {
            LandingScreen(windowSizeData = viewModel.windowSizeClass, onFinishedListener = {
                navController.navigate(Login.name){
                    popUpTo(Landing.name){inclusive = true}
                }
            })
        }
        composable(Login.name) {
            Text(text = "Login Screen567")
        }
    }
}