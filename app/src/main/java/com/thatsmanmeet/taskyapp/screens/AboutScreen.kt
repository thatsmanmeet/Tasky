package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.BuildConfig
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import com.thatsmanmeet.taskyapp.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingStore = SettingsStore(context)
    val savedThemeKey = settingStore.getThemeModeKey.collectAsState(initial = "")
    val savedFontKey = settingStore.getUseSystemFontKey.collectAsState(initial = false)
    val mainViewModel = viewModel<MainViewModel>()
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
                    title = { Text(text = "About Tasky") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.navigate(route = Screen.MyApp.route){
                                popUpTo(route = Screen.MyApp.route){
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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
                Text(text = "License: GPL-V3")
                Spacer(modifier = modifier.height(height))
                Text(text = "APP Version: V${BuildConfig.VERSION_NAME}")
                Spacer(modifier = modifier.height(height))
                Text(text = "APP Version Code: ${BuildConfig.VERSION_CODE}")
                Spacer(modifier = modifier.height(height))
                Text(text = "Author: @thatsmanmeet")
                Spacer(modifier = modifier.height(height))
                Text(text = "Click here to view Source Code", modifier = modifier.clickable {
                    mainViewModel.openGithubPage(context)
                }, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            }
        }
    }
