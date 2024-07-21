package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.BuildConfig
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    title:String = "Tasky",
    navHostController: NavHostController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    content : @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title)
                    Text(text = "V${BuildConfig.VERSION_NAME}")
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_check),modifier = modifier.size(20.dp), contentDescription = null) },
                    label = { Text(text = "Tasks", modifier = modifier.padding(start = 4.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(route = Screen.MyApp.route) {
                            this.popUpTo(Screen.MyApp.route) {
                                inclusive = true
                            }
                        }
                    }
                )
                NavigationDrawerItem(
                    modifier = modifier.padding(start = 16.dp, end = 16.dp),
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_notes),modifier = modifier.size(20.dp), contentDescription = null) },
                    label = { Text(text = "Notes", modifier = modifier.padding(start = 4.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(route = Screen.NotesScreen.route)
                    }
                )
                NavigationDrawerItem(
                    modifier = modifier.padding(start = 16.dp, end = 16.dp),
                    icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                    label = { Text(text = "Deleted Tasks") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(route = Screen.DeletedTodosScreen.route)
                    }
                )
                NavigationDrawerItem(
                    modifier = modifier.padding(start = 16.dp, end = 16.dp),
                    icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                    label = { Text(text = "Guide") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(route = Screen.GuideScreen.route)
                    }
                )
                NavigationDrawerItem(
                    modifier = modifier.padding(bottom = 16.dp,start = 16.dp, end = 16.dp),
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) },
                    label = { Text(text = "Settings") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(route = Screen.SettingsScreen.route)
                    }
                )
            }
        }
    ){
        content()
    }
}