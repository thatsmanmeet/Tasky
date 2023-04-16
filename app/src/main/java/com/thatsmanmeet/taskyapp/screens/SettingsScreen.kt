package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import com.thatsmanmeet.taskyapp.viewmodels.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    isChecked:State<Boolean?>,
    shouldShowAnimation:State<Boolean?>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingStore = SettingsStore(context)
    val isCheckedState = remember {
        mutableStateOf(isChecked.value)
    }
    val shouldShowAnimationState = remember {
        mutableStateOf(shouldShowAnimation.value)
    }
    val mainViewModel = MainViewModel()
    TaskyTheme{
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
                        Text(text = "Settings")
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
                Column(modifier = modifier
                    .fillMaxSize()
                    .padding(it)
                    .clip(
                        RoundedCornerShape(
                            topStart = 15.dp,
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp,
                            topEnd = 15.dp
                        )
                    ),
                ) {
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
                                text = "Separate tasks using dates",
                                fontSize = 18.sp,
                                modifier = modifier.weight(1f)
                            )
                            isCheckedState.value?.let { it1 ->
                                Switch(
                                    modifier = modifier.weight(0.2f),
                                    checked = it1,
                                    onCheckedChange = { isToggleChecked->
                                    isCheckedState.value = isToggleChecked
                                    scope.launch {
                                        settingStore.saveTaskListKey(isToggleChecked)
                                    }
                                })
                            }
                        }
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
                                text = "Use task complete animations",
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
                    // Auto Start card
                    Card(
                        modifier = modifier
                            .heightIn(56.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                            .clickable {
                                mainViewModel.enableAutoStartIntent(context)
                            },
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = modifier
                                    .weight(1f)
                                    .padding(10.dp)
                            ){
                                Text(
                                    text = "Enable Autostart",
                                    fontSize = 18.sp,

                                )

                                Text(
                                    text = "For some devices notifications might not receive on time or may not work after reboot. Click here to enable autostart",
                                    fontSize = 10.sp,
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.ic_battery),
                                contentDescription = "Battery Icon",
                                modifier = modifier
                                    .size(35.dp)
                                    .weight(0.1f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    // Visit Github Card
                    Spacer(modifier = modifier.height(12.dp))
                    Card(
                        modifier = modifier
                            .heightIn(56.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                            .clickable {
                                mainViewModel.openGithubPage(context)
                            },
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = modifier
                                    .weight(1f)
                                    .padding(10.dp)
                            ){
                                Text(
                                    text = "View Source",
                                    fontSize = 18.sp,

                                    )

                                Text(
                                    text = "Tasky is completely open source. Have a feedback or want to get into development ? visit Github! Oh and don't forget to give a ⭐️ ;)",
                                    fontSize = 10.sp,
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.ic_code),
                                contentDescription = "code icon",
                                modifier = modifier.size(35.dp).weight(0.1f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                     }
                }
            }
        }

    }
}
