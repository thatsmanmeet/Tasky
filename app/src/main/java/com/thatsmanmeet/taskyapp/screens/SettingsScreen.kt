package com.thatsmanmeet.taskyapp.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.MainActivity
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.components.ActionDialogBox
import com.thatsmanmeet.taskyapp.components.SettingsComponent
import com.thatsmanmeet.taskyapp.components.ThemeChangerDialog
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import com.thatsmanmeet.taskyapp.viewmodels.MainViewModel
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    isChecked:State<Boolean?>,
    shouldShowAnimation:State<Boolean?>,
    is24HourClockKey:State<Boolean?>,
    shouldPlaySound:State<Boolean?>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingStore = SettingsStore(context)
    val savedThemeKey = settingStore.getThemeModeKey.collectAsState(initial = "")
    val shouldShowAnimationState = remember {
        mutableStateOf(shouldShowAnimation.value)
    }
    val shouldPlaySoundState = remember{
        mutableStateOf(shouldPlaySound.value)
    }
    val is24HourClockState = remember {
        mutableStateOf(is24HourClockKey.value)
    }
    val isBackupDialogShowingState  = rememberSaveable {
        mutableStateOf(false)
    }
    val isThemeChangerShowing = rememberSaveable {
        mutableStateOf(false)
    }
    val mainViewModel = MainViewModel()
    val activity = LocalContext.current as Activity
    val dbPath = activity.getDatabasePath("todo_database").absolutePath
    TaskyTheme(darkTheme = when (savedThemeKey.value) {
        "0" -> {
            isSystemInDarkTheme()
        }
        "1" -> {false}
        else -> {true}
    }
    ){
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.navigate(route = Screen.MyApp.route){
                                popUpTo(route = Screen.MyApp.route){
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(text = stringResource(R.string.settings_screen_appbar_title))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) {
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .clip(
                            RoundedCornerShape(
                                topStart = 15.dp,
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp,
                                topEnd = 15.dp
                            )
                        )
                        .verticalScroll(rememberScrollState()),
                ) {
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.set_app_theme_settings_text),
                        settingText = stringResource(R.string.set_app_theme_info_text),
                        painterResourceID =  R.drawable.ic_phone
                    ) {
                        isThemeChangerShowing.value = true
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    // Use Animations Toggle
                    Card(
                        modifier = modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.use_task_complete_animations_title),
                                fontSize = 18.sp,
                                modifier = modifier.weight(1f)
                            )
                            shouldShowAnimationState.value?.let { it1 ->
                                Switch(
                                    modifier = modifier.weight(0.2f),
                                    checked = it1,
                                    onCheckedChange = { isToggleChecked->
                                        shouldShowAnimationState.value = isToggleChecked
                                        scope.launch {
                                            settingStore.saveAnimationShowKey(isToggleChecked)
                                        }
                                    })
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    // Use sound Toggle
                    Card(
                        modifier = modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.use_task_complete_sounds_title),
                                fontSize = 18.sp,
                                modifier = modifier.weight(1f)
                            )
                            shouldPlaySoundState.value?.let { it1 ->
                                Switch(
                                    modifier = modifier.weight(0.2f),
                                    checked = it1,
                                    onCheckedChange = { isToggleChecked->
                                        shouldPlaySoundState.value = isToggleChecked
                                        scope.launch {
                                            settingStore.saveSoundPlayKey(isToggleChecked)
                                        }
                                    })
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    // 24 Hour Clock
                    Card(
                        modifier = modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.use_24_hour_clock),
                                fontSize = 18.sp,
                                modifier = modifier.weight(1f)
                            )
                            is24HourClockState.value?.let { it1 ->
                                Switch(
                                    modifier = modifier.weight(0.2f),
                                    checked = it1,
                                    onCheckedChange = { isToggleChecked->
                                        is24HourClockState.value = isToggleChecked
                                        scope.launch {
                                            settingStore.saveClockKey(isToggleChecked)
                                        }
                                    })
                            }
                        }
                    }
                    // Auto Start card
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.enable_autostart_title),
                        settingText = stringResource(R.string.enable_autostart_information_text),
                        painterResourceID = R.drawable.ic_battery) {
                        mainViewModel.enableAutoStartIntent(context = context)
                    }
                    // Backup/Restore card
                    SettingsComponent(
                        settingHeaderText = stringResource(id = R.string.settings_backup_restore),
                        settingText = stringResource(id = R.string.settings_backup_restore_information_text),
                        painterResourceID = R.drawable.ic_history
                    ) {
                        isBackupDialogShowingState.value = true
                    }
                    // Visit Github Card
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.view_source_title),
                        settingText = stringResource(R.string.view_source_information_text),
                        painterResourceID = R.drawable.ic_code) {
                        mainViewModel.openGithubPage(context)
                    }
                }
            }
        }
        if(isBackupDialogShowingState.value){
            ActionDialogBox(
                isDialogShowing = isBackupDialogShowingState,
                title = stringResource(id = R.string.settings_backup_restore) ,
                message = stringResource(id = R.string.settings_backup_restore_information_text) ,
                confirmButtonText = "Restore",
                dismissButtonText = "Backup",
                onConfirmClick = {
                     MainActivity().restoreFile(context = activity,dbPath.toUri())
                },
                onDismissClick = {
                    MainActivity().writeFile(context = activity,dbPath.toUri())
                })
        }
        // Backup dialog ends here
        ThemeChangerDialog(
            selectedItem = remember {
            mutableStateOf(savedThemeKey.value.toString())
        },
            isShowing = isThemeChangerShowing
        ){mode->
            scope.launch {
                settingStore.saveThemeModeKey(mode)
            }
        }
    }
}
