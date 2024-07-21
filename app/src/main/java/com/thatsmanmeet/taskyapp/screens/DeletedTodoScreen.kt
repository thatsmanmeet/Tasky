package com.thatsmanmeet.taskyapp.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.thatsmanmeet.taskyapp.components.DeletedTodoItem
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.TodoViewModel
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
    val todoViewModel = TodoViewModel(activity.application)
    val deletedTodoList = deletedTodoViewModel.getAllDeletedTodos.collectAsState(initial = emptyList())
    val savedFontKey = settingStore.getUseSystemFontKey.collectAsState(initial = false)
    TaskyTheme(darkTheme = when (savedThemeKey.value) {
        "0" -> {
            isSystemInDarkTheme()
        }
        "1" -> {false}
        else -> {true}
    },
        useSystemFont = savedFontKey.value!!) {
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
                 Text(text = "Deleted Tasks will be stay here as an archived storage. You can tap on a task to restore or delete it permanently.", fontSize = 15.sp)
             }
                LazyColumn{
                    items(deletedTodoList.value){deletedTodo->
                        DeletedTodoItem(
                            deletedTodo = deletedTodo,
                            todoViewModel = todoViewModel,
                            deletedTodoViewModel = deletedTodoViewModel
                        )
                        Spacer(modifier = modifier.height(5.dp))
                    }

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