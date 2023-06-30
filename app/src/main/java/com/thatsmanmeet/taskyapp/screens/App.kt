package com.thatsmanmeet.taskyapp.screens

import android.annotation.SuppressLint
import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.thatsmanmeet.taskyapp.components.TaskCompleteAnimations
import com.thatsmanmeet.taskyapp.components.TaskList
import com.thatsmanmeet.taskyapp.components.addTodoDialog
import com.thatsmanmeet.taskyapp.constants.Constants
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.notification.Notification
import com.thatsmanmeet.taskyapp.notification.channelID
import com.thatsmanmeet.taskyapp.receiver.RepeatingTasksReceiver
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("AutoboxingStateValueProperty")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val todoViewModel = TodoViewModel(activity.application)
    val listState = rememberLazyListState()
    val selectedItem = rememberSaveable {
        mutableIntStateOf(-1)
    }
    val todoListFromFlow by todoViewModel.getAllTodosFlow.collectAsState(initial = emptyList())
    val topAppBarColors = TopAppBarDefaults
    val openDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val openEditDialog = rememberSaveable {
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
    val isRepeatingState = remember{
        mutableStateOf(false)
    }
    val timeText = remember {
        mutableStateOf("")
    }
    val isTimeDialogShowing = remember {
        mutableStateOf(false)
    }
    val isLottiePlaying = remember {
        mutableStateOf(true)
    }
    createNotificationChannel(context.applicationContext)
    // setup settings store
    val settingsStore = SettingsStore(context)
    val savedTaskKey = settingsStore.getTaskListKey.collectAsState(initial = true)
    val savedAnimationKey = settingsStore.getAnimationKey.collectAsState(initial = true)
    TaskyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = modifier.fillMaxWidth(),
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
                    text = { Text(text = stringResource(R.string.add_task_button)) },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_add_task), contentDescription = null) },
                    onClick = {
                        openDialog.value = true
                    },
                    expanded = listState.isScrollingUp()
                )
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
                todoViewModel,
                isRepeatingState.value
            )
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if(todoListFromFlow.isEmpty()){
                    Box(modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Column(
                            modifier = modifier,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = modifier
                                    .size(50.dp)
                                    .alpha(0.8f),
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.empty_list_no_tasks_text),
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
                            list = todoListFromFlow,
                            todoViewModel = todoViewModel,
                            onClick = {index->
                                selectedItem.value = index
                                openEditDialog.value = true
                            }
                        )
                    }else{
                        LegacyTaskList(
                            state = listState,
                            list = todoListFromFlow,
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
                        todoListFromFlow,
                        selectedItem,
                        openEditDialog,
                        todoViewModel,
                        enteredText,
                        context
                    )
                }
            }

        }
        if(todoViewModel.isAnimationPlayingState.value && (savedAnimationKey.value == true || savedAnimationKey.value == null)){
            Column(modifier = modifier.fillMaxSize()) {
                TaskCompleteAnimations(
                    isLottiePlaying = isLottiePlaying,
                    modifier = modifier.fillMaxSize()
                )
                LaunchedEffect(Unit){
                    delay(2200)
                    withContext(Dispatchers.Main){
                        todoViewModel.isAnimationPlayingState.value = false
                    }

                }
            }
        }
    }
}


fun createNotificationChannel(context: Context){
    val name = "Reminder"
    val desc = "Sends Notifications of the tasks added to the list"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelID,name,importance)
    val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
    channel.description = desc
    channel.enableLights(true)
    channel.enableVibration(true)
    channel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.notifications),attributes)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (notificationManager.getNotificationChannel("Remainder Channel") != null){
        notificationManager.deleteNotificationChannel("Remainder Channel")
    }
    if(notificationManager.notificationChannels.isNullOrEmpty()){
        notificationManager.createNotificationChannel(channel)
    }
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
        todo.notificationID,
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
fun setRepeatingAlarm(context: Context){
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val tomorrowTimestamp = calendar.timeInMillis
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val intent = Intent(context.applicationContext,RepeatingTasksReceiver::class.java).also {
        it.action = "repeating_tasks"
    }
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        tomorrowTimestamp,
        PendingIntent.getBroadcast(context.applicationContext,Constants.BROADCAST_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    )
}

fun cancelNotification(
    context: Context,
    titleText:String?,
    messageText:String?,
    todo: Todo
){
    val intent = Intent(context, Notification::class.java)
    intent.putExtra("titleExtra", titleText)
    intent.putExtra("messageExtra", messageText)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        todo.notificationID,
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

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
