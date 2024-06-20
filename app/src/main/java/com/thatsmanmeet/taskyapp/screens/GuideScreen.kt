package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingStore = SettingsStore(context)
    val savedThemeKey = settingStore.getThemeModeKey.collectAsState(initial = "")
    val savedFontKey = settingStore.getUseSystemFontKey.collectAsState(initial = false)
    val height = 12.dp
    TaskyTheme(
        darkTheme = when (savedThemeKey.value) {
            "0" -> {
                isSystemInDarkTheme()
            }
            "1" -> {false}
            else -> {true}
        },
        useSystemFont = savedFontKey.value!!
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Guide") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.navigate(route = Screen.MyApp.route){
                                popUpTo(route = Screen.MyApp.route){
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) {paddingValues ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
               Text(
                   text = "Welcome to Tasky, a free and open source, minimal and aesthetic task management app. This guide will provide you with everything you can do in this app.",
                   fontSize = 16.sp,
                   textAlign = TextAlign.Justify
               )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "1. On very first start, you will be greeted with a permission screen (if your android version is 13 or above). Here the app will ask for notification permission that is required to provide you with notifications on time.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "2. You will be on the task screen. Here the navigation bar have 3 things (menu icon, app name and search icon). On the screen you have a add tasks button and an empty list.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "3. You can click on add task and an dialog will open where you have to enter task name (description is optional). You can set date and time of the task and can set if the notification should be repeated daily or not just like a routine habit tracker. You can save task by clicking Add button. Notification will display on set date (everyday if repeating option is checked)/time and notification will contain tasks title and description.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "4. You will see the task added on the screen. You can tap on the task and an edit dialog will open where you can edit or delete the task.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "5. You can also swipe an individual task. Swiping left to right on task will mark task as complete/uncompleted and swiping from right to left will delete the task and will add it to trash.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "6. On top you can see a search icon. You can click on it and search for a task. This will search both task and description of all the tasks.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "7. On top you can see a search icon. You can click on it and search for a task. This will search both task and description of all the tasks.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "8. On Clicking Menu button a navigation drawer will open and contains app name and version code. It contains three items (Deleted Tasks, Guide and Settings).",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "9. On Deleted Tasks Screen you can see all the tasks you deleted. You can only tap on the item to either delete them permanently or restore them. You cannot edit them. Also if they are completed they have line-through the text. ",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "10. Guide button will bring you to this guide. ",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "11. Settings button will take you to settings screen where you can tweak various settings. ",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = modifier.height(height))
                Text(
                    text = "12. In Settings screen, you can set app theme, can enable/disable confetti animations on task complete, can enable/disable task completion sounds, switch to 12/24 hour clock, backup/restore tasks locally and enable autostart.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            }
        }
    }
