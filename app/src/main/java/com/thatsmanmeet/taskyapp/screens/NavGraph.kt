package com.thatsmanmeet.taskyapp.screens


import android.transition.Fade
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
    val useLegacyDateTimePickersState = settingsStore.getUseLegacyDateTimePickers.collectAsState(initial = false)
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
            route = Screen.SettingsScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ){
            SettingsScreen(
                navController,
                isChecked = isEnabledState,
                shouldShowAnimation = isAnimationShowingState,
                is24HourClockKey = is24HourClock,
                shouldPlaySound = usSoundPlayingState,
                useSystemFont = useSystemFontState,
                useLegacyDateTimePickers = useLegacyDateTimePickersState
            )
        }
        composable(
            route = Screen.MyApp.route,
            enterTransition = {
                return@composable fadeIn(tween(700))
            },
        ){
            MyApp(navHostController = navController)
        }
        
        composable(
            route = Screen.DeletedTodosScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ){
            DeletedTodoScreen(navHostController = navController)
        }

        composable(
            route = Screen.AboutScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ){
            AboutScreen(navHostController = navController)
        }

        composable(route = Screen.NotesScreen.route){
            NotesScreen(navHostController = navController)
        }
        composable(
            route = Screen.AddNotesScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
            ){
            AddNoteScreen(navHostController = navController)
        }

        composable(
            route = Screen.EditNotesScreen.route,
            arguments = listOf(navArgument("noteID"){
                type = NavType.LongType
            }),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ){
            val noteId = it.arguments?.getLong("noteID")
            EditNoteScreen(navHostController = navController, noteID = noteId)
        }

    }
}