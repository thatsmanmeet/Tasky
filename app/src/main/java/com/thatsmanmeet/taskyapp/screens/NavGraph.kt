package com.thatsmanmeet.taskyapp.screens


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thatsmanmeet.taskyapp.datastore.SettingsStore


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
    val useSystemFontState = settingsStore.getUseSystemFontKey.collectAsState(initial = false)
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
                shouldPlaySound = usSoundPlayingState,
                useSystemFont = useSystemFontState
            )
        }
        composable(
            route = Screen.MyApp.route
        ){
            MyApp(navHostController = navController)
        }
        
        composable(route = Screen.DeletedTodosScreen.route){
            DeletedTodoScreen(navHostController = navController)
        }

        composable(route = Screen.AboutScreen.route){
            AboutScreen(navHostController = navController)
        }

        composable(route = Screen.NotesScreen.route){
            NotesScreen(navHostController = navController)
        }
        composable(route = Screen.AddNotesScreen.route){
            AddNoteScreen(navHostController = navController)
        }

        composable(
            route = Screen.EditNotesScreen.route,
            arguments = listOf(navArgument("noteID"){
                type = NavType.LongType
            })
        ){
            val noteId = it.arguments?.getLong("noteID")
            EditNoteScreen(navHostController = navController, noteID = noteId)
        }

    }
}