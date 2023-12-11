package com.thatsmanmeet.taskyapp.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodoViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletedTodoScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as Activity
    val settingStore = SettingsStore(context)
    val savedThemeKey = settingStore.getThemeModeKey.collectAsState(initial = "")
    val deletedTodoViewModel = DeletedTodoViewModel(activity.application)
    val deletedTodoList = deletedTodoViewModel.getAllDeletedTodos.collectAsState(initial = emptyList())

    TaskyTheme(darkTheme = when (savedThemeKey.value) {
        "0" -> {
            isSystemInDarkTheme()
        }
        "1" -> {false}
        else -> {true}
    }) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                    Text(text = "Deleted Tasks")
                }, navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigate(route = Screen.MyApp.route){
                            popUpTo(route = Screen.MyApp.route){
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) {paddingValues ->
            Column(
                modifier = modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
             Box(
                 modifier = modifier
                 .fillMaxWidth()
                 .height(100.dp)
                 .padding(16.dp)
             ){
                 Text(text = "Deleted Todos will be automatically deleted in 30 days.", fontSize = 15.sp)
             }

            }
        }
    }

}

@Preview
@Composable
fun PreviewDeletedTodoScreen() {
    DeletedTodoScreen(rememberNavController())
}