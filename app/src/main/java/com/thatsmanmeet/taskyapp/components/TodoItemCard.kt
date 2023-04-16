package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.screens.cancelNotification
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .heightIn(min = 60.dp)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
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
                        todo.time
                    )
                    viewModel.updateTodo(
                        currentTodo
                    )
                    if(currentCheckBoxState.value){
                        viewModel.playCompletedSound(context)
                        viewModel.isAnimationPlayingState.value = true
                            cancelNotification(
                                context = context,
                                titleText = currentTodo.title,
                                messageText = "Have you completed your task today ?",
                                time = "${currentTodo.date} ${currentTodo.time}",
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
                                    messageText = "Have you completed your task today ?",
                                    time = "${currentTodo.date} ${currentTodo.time}",
                                    todo = currentTodo
                                )
                            }
                        }
                    }
                })
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = todo.title!!,
                textDecoration = if (currentCheckBoxState.value) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = 16.sp
            )
        }
        if(todo.time!!.isNotEmpty()){
            Icon(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(24.dp),
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
