package com.thatsmanmeet.taskyapp.screens

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.components.TodoItemCard
import com.thatsmanmeet.taskyapp.components.showDatePicker
import com.thatsmanmeet.taskyapp.components.showTimePickerDialog
import com.thatsmanmeet.taskyapp.notification.Notification
import com.thatsmanmeet.taskyapp.notification.channelID
import com.thatsmanmeet.taskyapp.notification.notificationID
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
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
    TaskyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontSize = 20.sp
                        )
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
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    onClick = {
                        openDialog.value = true
                    })
            }
        ) { paddingValues ->
            if(openDialog.value){
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                        enteredText = ""
                    },
                    title = { Text(text = "Add Task") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = enteredText,
                                placeholder = { Text(text = "what's on your mind?") },
                                onValueChange = {textChange ->
                                    enteredText = textChange
                                },
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Set Reminder (optional)")
                            Spacer(modifier = Modifier.height(10.dp))
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Icon(imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = dateText.value)
                                }
                                OutlinedButton(modifier = Modifier.height(35.dp)
                                    ,onClick = {
                                        isDateDialogShowing.value = true
                                    }) {
                                    Text(text = "Select Date", fontSize = 10.sp)
                                    val date = showDatePicker(context = context,isDateDialogShowing)
                                    dateText.value = date
                                    isDateDialogShowing.value = false
                                }
                            }
                            // time
                            Spacer(modifier = Modifier.height(10.dp))
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Icon(imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = timeText.value)
                                }
                                OutlinedButton(modifier = Modifier.height(35.dp)
                                    ,onClick = {
                                        isTimeDialogShowing.value = true
                                    }) {
                                    Text(text = "Select Time", fontSize = 10.sp)
                                    val date = showTimePickerDialog(context = context,isTimeDialogShowing)
                                    timeText.value = date
                                    isTimeDialogShowing.value = false
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            openDialog.value = false
                            todoViewModel.insertTodo(
                                Todo(
                                    ID= null,
                                    enteredText.ifEmpty { "No Name" },
                                    isCompleted = false,
                                    dateText.value.ifEmpty {
                                        SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).format(
                                            Calendar.getInstance().time
                                        ).toString()
                                    },
                                    timeText.value)
                            )
                            if(dateText.value.isNotEmpty() && timeText.value.isNotEmpty()){
                                // SCHEDULE NOTIFICATION
                                scheduleNotification(
                                    context,
                                    titleText = enteredText,
                                    messageText = "Did you complete your Task ?",
                                    time="${dateText.value} ${timeText.value}"
                                )
                            }
                            enteredText = ""
                        }) {
                            Text(text = "Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            openDialog.value = false
                            enteredText = ""
                        }) {
                            Text(text = "Cancel")
                        }
                    })
            }
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
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        itemsIndexed(todosList.value) { index, item ->
                            val movableContent = movableContentOf {
                                TodoItemCard(
                                    todo = item,
                                    viewModel = todoViewModel,
                                    modifier = Modifier
                                        .clickable {
                                            selectedItem.value = index
                                            openEditDialog.value = true
                                        })
                            }
                            movableContent()
                        }
                    }
                }
                // LazyColumn ends here
                if (openEditDialog.value){
                    val currentTodoTitle = remember {
                        mutableStateOf(todosList.value[selectedItem.value].title)
                    }
                    val currentTodoID = remember {
                        mutableStateOf(todosList.value[selectedItem.value].ID)
                    }

                    val currentTodoChecked = remember {
                        mutableStateOf(todosList.value[selectedItem.value].isCompleted)
                    }

                    val currentTodoDateValue = remember {
                        mutableStateOf(todosList.value[selectedItem.value].date)
                    }

                    val currentTodoTimeValue = remember {
                        mutableStateOf(todosList.value[selectedItem.value].time)
                    }

                    AlertDialog(
                        onDismissRequest = {
                            openEditDialog.value = false
                        },
                        title = { Text(text = "Edit Task") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = currentTodoTitle.value!!,
                                    placeholder = { Text(text = "what's on your mind?") },
                                    onValueChange = {textChange ->
                                        currentTodoTitle.value = textChange
                                    }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Reminder")
                                Spacer(modifier = Modifier.height(10.dp))
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = todosList.value[selectedItem.value].date!!)
                                    }
                                }
                                // time
                                Spacer(modifier = Modifier.height(10.dp))
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Icon(imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = todosList.value[selectedItem.value].time!!)
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                openEditDialog.value = false
                                if(currentTodoDateValue.value.isNullOrEmpty()){
                                    currentTodoDateValue.value = todosList.value[selectedItem.value].date
                                }
                                if(currentTodoTimeValue.value.isNullOrEmpty()){
                                    currentTodoTimeValue.value = todosList.value[selectedItem.value].time
                                }
                                todoViewModel.updateTodo(
                                    Todo(
                                        currentTodoID.value,
                                        currentTodoTitle.value?.ifEmpty {
                                            "No Name"
                                        },
                                        currentTodoChecked.value,
                                        currentTodoDateValue.value,
                                        currentTodoTimeValue.value
                                    )
                                )
                                enteredText = ""
                            }) {
                                Text(text = "Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                openEditDialog.value = false
                                todoViewModel.deleteTodo(
                                    Todo(
                                        currentTodoID.value,
                                        currentTodoTitle.value,
                                        currentTodoChecked.value,
                                        currentTodoDateValue.value,
                                        currentTodoTimeValue.value
                                    )
                                )
                                enteredText = ""
                                todoViewModel.playDeletedSound(context)
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFFF1574C
                                ), contentColor = Color.White)
                            ) {
                                Text(text = "Delete")
                            }
                        })
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
    time:String?
) {

    val intent = Intent(context, Notification::class.java)
    intent.putExtra("titleExtra", titleText)
    intent.putExtra("messageExtra", messageText)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationID,
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
fun getTimeInMillis(date: String): Long {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH)
    val mDate = sdf.parse(date)
    return mDate!!.time
}
