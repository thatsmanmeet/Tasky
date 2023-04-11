package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    isChecked:State<Boolean?>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingStore = SettingsStore(context)
    val isCheckedState = remember {
        mutableStateOf(isChecked.value)
    }
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
                modifier = modifier.fillMaxSize().padding(16.dp)
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
                }
            }

        }
    }
}
