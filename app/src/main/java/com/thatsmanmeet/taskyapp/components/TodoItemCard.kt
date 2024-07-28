package com.thatsmanmeet.taskyapp.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.screens.cancelNotification
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@Composable
fun TodoItemCard(
    todo: Todo,
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
){
    val currentCheckBoxState = remember {
        mutableStateOf(todo.isCompleted)
    }
    val context = LocalContext.current
    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    val settingsStore = SettingsStore(context = context)
    val savedSoundKey = settingsStore.getSoundKey.collectAsState(initial = true)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .heightIn(min = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = currentCheckBoxState.value,
                onCheckedChange = {
                    currentCheckBoxState.value = it
                    val currentTodo = Todo(
                        todo.ID,
                        todo.title,
                        currentCheckBoxState.value,
                        todo.date,
                        todo.time,
                        notificationID = todo.notificationID,
                        isRecurring = todo.isRecurring,
                        todoDescription = todo.todoDescription
                    )
                    viewModel.updateTodo(
                        currentTodo
                    )
                    if(currentCheckBoxState.value){
                        if(savedSoundKey.value == true){
                            viewModel.playCompletedSound(context)
                        }
                        viewModel.isAnimationPlayingState.value = true
                            cancelNotification(
                                context = context,
                                todo = currentTodo
                            )
                    }else{
                        if (currentTodo.date!!.isNotEmpty() && currentTodo.time!!.isNotEmpty()){
                            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedDate = format.parse(currentTodo.date!!)
                            val calendar = Calendar.getInstance().apply {
                                time = parsedDate!!
                                set(Calendar.HOUR_OF_DAY, currentTodo.time!!.substringBefore(":").toInt())
                                set(Calendar.MINUTE, currentTodo.time!!.substringAfter(":").toInt())
                                set(Calendar.SECOND, 0)
                            }
                            val currentTime = Calendar.getInstance().timeInMillis
                            if(calendar >= currentDate && calendar.timeInMillis >= currentTime){
                                scheduleNotification(
                                    context = context,
                                    titleText = currentTodo.title,
                                    messageText = currentTodo.todoDescription,
                                    time = "${currentTodo.date} ${currentTodo.time}",
                                    todo = currentTodo
                                )
                            }
                        }
                    }
                })
            Spacer(modifier = modifier.width(3.dp))
            Text(
                modifier = modifier.fillMaxWidth(0.9f),
                text = todo.title!!,
                textDecoration = if (currentCheckBoxState.value) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = 16.sp
            )
        }
        if(todo.time!!.isNotEmpty()){
            if(todo.isRecurring){
                Icon(
                    modifier = modifier
                        .padding(end = 5.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }else if(!todo.isCompleted){
                Icon(
                    modifier = modifier
                        .padding(end = 5.dp)
                        .size(24.dp),
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    Spacer(modifier = modifier.height(12.dp))
}
