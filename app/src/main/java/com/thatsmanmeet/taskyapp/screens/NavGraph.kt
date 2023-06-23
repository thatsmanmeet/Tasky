package com.thatsmanmeet.taskyapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thatsmanmeet.taskyapp.datastore.SettingsStore


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController:NavHostController
) {
    val context = LocalContext.current
    val settingsStore = SettingsStore(context)
    val isEnabledState = settingsStore.getTaskListKey.collectAsState(initial = false)
    val isAnimationShowingState = settingsStore.getAnimationKey.collectAsState(initial = false)
    val is24HourClock = settingsStore.getClockKey.collectAsState(initial = false)
    val usSoundPlayingState = settingsStore.getSoundKey.collectAsState(initial = false)
    NavHost(
        navController = navController,
        startDestination = Screen.PermissionScreen.route
    ){
        composable(
            route = Screen.PermissionScreen.route
        ){
            PermissionRequestScreen(navController, requestOnClick = {

            })
        }

        composable(
            route = Screen.SettingsScreen.route
        ){
            SettingsScreen(
                navController,
                isChecked = isEnabledState,
                shouldShowAnimation = isAnimationShowingState,
                is24HourClockKey = is24HourClock,
                shouldPlaySound = usSoundPlayingState
            )
        }
        composable(
            route = Screen.MyApp.route
        ){
            MyApp(navHostController = navController)
        }
    }
}