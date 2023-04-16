package com.thatsmanmeet.taskyapp.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun addTodoDialog(
    openDialog: MutableState<Boolean>,
    enteredText: String,
    dateText: MutableState<String>,
    isDateDialogShowing: MutableState<Boolean>,
    context: Context,
    timeText: MutableState<String>,
    isTimeDialogShowing: MutableState<Boolean>,
    todoViewModel: TodoViewModel,
    modifier: Modifier = Modifier
): String {
    var enteredText1 by remember {
        mutableStateOf(enteredText)
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                enteredText1 = ""
            },
            title = { Text(text = "Add Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = enteredText1,
                        placeholder = { Text(text = "what's on your mind?") },
                        onValueChange = { textChange ->
                            enteredText1 = textChange
                        },
                        maxLines = 1
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Text(text = "Set Reminder (optional)")
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = modifier.width(5.dp))
                            Text(text = dateText.value)
                        }
                        OutlinedButton(modifier = modifier.height(35.dp), onClick = {
                            isDateDialogShowing.value = true
                        }) {
                            Text(text = "Select Date", fontSize = 10.sp)
                            val date = showDatePicker(context = context, isDateDialogShowing)
                            dateText.value = date
                            isDateDialogShowing.value = false
                        }
                    }
                    // time
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = modifier.width(5.dp))
                            Text(text = timeText.value)
                        }
                        OutlinedButton(modifier = modifier.height(35.dp), onClick = {
                            isTimeDialogShowing.value = true
                        }) {
                            Text(text = "Select Time", fontSize = 10.sp)
                            val date = showTimePickerDialog(context = context, isTimeDialogShowing)
                            timeText.value = date
                            isTimeDialogShowing.value = false
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    openDialog.value = false
                    val todo = Todo(
                        ID = null,
                        enteredText1.ifEmpty { "No Name" },
                        isCompleted = false,
                        date = dateText.value.ifEmpty {
                            SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(
                                Calendar.getInstance().time
                            ).toString()
                        },
                        time = timeText.value,
                        notificationID = ((0..2000).random() - (0..50).random()),
                        isRecurring = false
                    )
                    todoViewModel.insertTodo(
                        todo
                    )
                    if (dateText.value.isNotEmpty() && timeText.value.isNotEmpty()) {
                        // SCHEDULE NOTIFICATION
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDate = format.parse(dateText.value)
                        val calendar = Calendar.getInstance().apply {
                            time = parsedDate!!
                            set(Calendar.HOUR_OF_DAY, todo.time!!.substringBefore(":").toInt())
                            set(Calendar.MINUTE, todo.time!!.substringAfter(":").toInt())
                            set(Calendar.SECOND, 0)
                        }
                        val currentTime = Calendar.getInstance().timeInMillis
                        if(calendar.timeInMillis >= currentTime){
                            scheduleNotification(
                                context,
                                titleText = enteredText1,
                                messageText = "Did you complete your Task ?",
                                time = "${dateText.value} ${timeText.value}",
                                todo = todo
                            )
                        }
                    }
                    enteredText1 = ""
                }
                ) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                    enteredText1 = ""
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1574C),
                        contentColor = Color.White
                    )) {
                    Text(text = "Cancel")
                }
            })
    }
    return enteredText1
}