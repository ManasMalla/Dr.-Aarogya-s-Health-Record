package com.manasmalla.draarogyashealthrecord.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.ui.screens.HealthRecordDestinations.*
import com.manasmalla.draarogyashealthrecord.ui.screens.LoginScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.SplashScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.home.HomeScreen
import com.manasmalla.draarogyashealthrecord.ui.screens.home.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordApp(
    userViewModel: UserViewModel, homeViewModel: HomeViewModel, startDestination: String = SplashDestination.toString(),modifier: Modifier = Modifier
) {

    //The NavController to help control the Navigation
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
                startDestination = startDestination,
            ) {

                composable(SplashDestination.toString()) {
//                    LaunchedEffect(key1 = !isFirstRuntime) {
//                        if (!isFirstRuntime) {
//                            delay(1_000)
//                            //Check if current user exists
//                            navController.navigate(HomeDestination.toString())
//                        }
//                    }
                    SplashScreen(isFirstRuntime = true, onGetStarted = {
                        navController.navigate(LoginDestination.toString())
                    })
                }
                composable(LoginDestination.toString()) {
                    LoginScreen(userUiState = userViewModel.uiState,
                        updateUiState = userViewModel::updateUiState,
                        onRegisterUser = {
                            userViewModel.registerUser {
                                navController.navigate(HomeDestination.toString())
                            }
                        })
                }

                composable(HomeDestination.toString()) {
                    HomeScreen(
                        userViewModel = userViewModel,
                        homeViewModel = homeViewModel,
                        onAddUser = {
                            userViewModel.updateUiState(
                                UserUiState(
                                    name = "",
                                    gender = Gender.Male,
                                    age = ""
                                )
                            )
                            navController.navigate(AddUserDestination.toString())
                        },
                        onManageProfile = {
                            userViewModel.updateUiStateToCurrentUser()
                            navController.navigate(ManageProfileDestination.toString())
                        })

                }

                composable(ManageProfileDestination.toString()) {
                    LoginScreen(
                        userUiState = userViewModel.uiState,
                        updateUiState = userViewModel::updateUiState,
                        onRegisterUser = {
                            userViewModel.updateUser {
                                navController.navigateUp()
                            }
                        },
                        primaryActionLabel = "Update Profile",
                        secondaryActions = {
                            OutlinedButton(
                                onClick = {
                                    userViewModel.deleteCurrentUser {
                                        navController.navigateUp()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, bottom = 24.dp)
                            ) {
                                Text(text = "Delete Account")
                            }
                        },
                        hasSecondaryActions = true
                    )
                }

                composable(AddUserDestination.toString()) {
                    LoginScreen(
                        userUiState = userViewModel.uiState,
                        updateUiState = userViewModel::updateUiState,
                        onRegisterUser = {
                            userViewModel.registerAnotherUser {
                                navController.popBackStack(HomeDestination.toString(), false)
                            }
                        },
                        primaryActionLabel = "Register"
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun HealthRecordAppPreview() {
//    DrAarogyasHealthRecordTheme {
//        HealthRecordApp()
//    }
//}