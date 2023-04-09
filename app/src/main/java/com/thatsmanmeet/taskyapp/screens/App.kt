package com.thatsmanmeet.taskyapp.screens

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.components.LegacyTaskList
import com.thatsmanmeet.taskyapp.components.OpenEditTodoDialog
import com.thatsmanmeet.taskyapp.components.TaskList
import com.thatsmanmeet.taskyapp.components.addTodoDialog
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.notification.Notification
import com.thatsmanmeet.taskyapp.notification.channelID
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(navHostController: NavHostController) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val todoViewModel = TodoViewModel(activity.application)
    val listState = rememberLazyListState()
    val selectedItem = remember {
        mutableStateOf(0)
    }
    val todosList = todoViewModel.getAllTodos.observeAsState(initial = listOf())
    val topAppBarColors = TopAppBarDefaults
    val openDialog = remember {
        mutableStateOf(false)
    }
    val openEditDialog = remember {
        mutableStateOf(false)
    }
    var enteredText by remember {
        mutableStateOf("")
    }
    val dateText = remember {
        mutableStateOf("")
    }
    val isDateDialogShowing = remember {
        mutableStateOf(false)
    }

    val timeText = remember {
        mutableStateOf("")
    }
    val isTimeDialogShowing = remember {
        mutableStateOf(false)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context)
    }
    // setup settings store
    val settingsStore = SettingsStore(context)
    val savedTaskKey = settingsStore.getTaskListKey.collectAsState(initial = true)
    TaskyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                fontSize = 30.sp
                            )
                            IconButton(onClick = {
                                // Implement Navigation to settings
                                navHostController.navigate(route = Screen.SettingsScreen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                            },
                    colors = topAppBarColors.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Add Task") },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_add_task), contentDescription = null) },
                    onClick = {
                        openDialog.value = true
                    })
            }
        ) { paddingValues ->
            enteredText = addTodoDialog(
                openDialog,
                enteredText,
                dateText,
                isDateDialogShowing,
                context,
                timeText,
                isTimeDialogShowing,
                todoViewModel
            )
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if(todosList.value.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .alpha(0.8f),
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "No Tasks",
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }else {
                    if(savedTaskKey.value == null || savedTaskKey.value == true){
                        TaskList(
                            state = listState,
                            list = todosList.value.toMutableStateList(),
                            todoViewModel = todoViewModel,
                            onClick = {index->
                                selectedItem.value = index
                                openEditDialog.value = true
                            }
                        )
                    }else{
                        LegacyTaskList(
                            state = listState,
                            list = todosList.value.toMutableStateList(),
                            todoViewModel = todoViewModel,
                            onClick = {index->
                                selectedItem.value = index
                                openEditDialog.value = true
                            }
                        )
                    }

                }
                if (openEditDialog.value){
                    OpenEditTodoDialog(
                        todosList,
                        selectedItem,
                        openEditDialog,
                        todoViewModel,
                        enteredText,
                        context
                    )
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(context: Context){
    val name = "Reminder"
    val desc = "Sends Notifications of the tasks added to the list"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelID,name,importance)
    channel.description = desc
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
fun scheduleNotification(
    context: Context,
    titleText:String?,
    messageText:String?,
    time:String?,
    todo: Todo
) {

    val intent = Intent(context, Notification::class.java)
    intent.putExtra("titleExtra", titleText)
    intent.putExtra("messageExtra", messageText)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        todo.hashCode(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val currTime = getTimeInMillis(time!!)
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        currTime,
        pendingIntent
    )
}

fun cancelNotification(
    context: Context,
    titleText:String?,
    messageText:String?,
    time:String?,
    todo: Todo
){
    val intent = Intent(context, Notification::class.java)
    intent.putExtra("titleExtra", titleText)
    intent.putExtra("messageExtra", messageText)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        todo.hashCode(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}
fun getTimeInMillis(date: String): Long {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH)
    val mDate = sdf.parse(date)
    return mDate!!.time
}
