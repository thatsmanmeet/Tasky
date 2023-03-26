package com.thatsmanmeet.taskyapp


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thatsmanmeet.taskyapp.screens.PermissionRequestScreen
import com.thatsmanmeet.taskyapp.screens.Screen
import com.thatsmanmeet.taskyapp.screens.SetupNavGraph
import com.thatsmanmeet.taskyapp.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController : NavHostController
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val context = LocalContext.current
            val viewModel = MainViewModel()
            val pageState = remember {
                mutableStateOf(ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            }
            navController = rememberNavController()
            SetupNavGraph(navController = navController)
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {isGranted->
                    viewModel.onPermissionResult(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        isGranted = isGranted
                    )
                    if (isGranted) {
                        pageState.value = true
                        navController.navigate(route = Screen.MyApp.route) {
                            popUpTo(Screen.PermissionScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
            val checkPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                pageState.value = true
            }else {
                if (checkPermission) {
                    pageState.value = true
                } else {
                    PermissionRequestScreen(navHostController = navController) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
            // If permissions are already accepted.
            if(pageState.value){
                navController.navigate(route = Screen.MyApp.route){
                    popUpTo(Screen.PermissionScreen.route){
                        inclusive = true
                    }
                }
            }
        }
    }
}