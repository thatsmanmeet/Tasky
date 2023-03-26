package com.thatsmanmeet.taskyapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun SetupNavGraph(
    navController:NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PermissionScreen.route
    ){
        composable(
            route = Screen.PermissionScreen.route
        ){
            PermissionRequestScreen(navController) {

            }
        }

        composable(
            route = Screen.MyApp.route
        ){
            MyApp()
        }
    }
}