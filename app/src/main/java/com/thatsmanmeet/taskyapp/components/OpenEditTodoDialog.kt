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
import com.thatsmanmeet.taskyapp.screens.cancelNotification
import com.thatsmanmeet.taskyapp.screens.scheduleNotification

@Composable
fun OpenEditTodoDialog(
    todosList: State<List<Todo>>,
    selectedItem: MutableState<Int>,
    openEditDialog: MutableState<Boolean>,
    todoViewModel: TodoViewModel,
    enteredText: String,
    context: Context
) {
    var enteredText1 by remember {
        mutableStateOf(enteredText)
    }
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

    val currentTodoNotificationId = remember {
        mutableStateOf(todosList.value[selectedItem.value].notificationID)
    }

    val isDateDialogShowing = remember {
        mutableStateOf(false)
    }

    val isTimeDialogShowing = remember {
        mutableStateOf(false)
    }

    var todo : Todo = Todo()

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
                    onValueChange = { textChange ->
                        currentTodoTitle.value = textChange
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Edit Reminder")
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        Spacer(modifier = Modifier.width(5.dp))
                        if(currentTodoDateValue.value.isNullOrEmpty()){
                            Text(text = todosList.value[selectedItem.value].date!!)
                        }else{
                            Text(text = currentTodoDateValue.value!!)
                        }
                    }
                    // TODO - Modify select date
                    OutlinedButton(modifier = Modifier.height(35.dp), onClick = {
                        isDateDialogShowing.value = true
                    }) {
                        Text(text = "Select Date", fontSize = 10.sp)
                        val date = showDatePicker(context = context, isShowing = isDateDialogShowing)
                        currentTodoDateValue.value = date
                        isTimeDialogShowing.value = false
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        Spacer(modifier = Modifier.width(5.dp))
                        if(currentTodoTimeValue.value.isNullOrEmpty()){
                            Text(text = todosList.value[selectedItem.value].time!!)
                        }else{
                            Text(text = currentTodoTimeValue.value!!)
                        }
                    }
                    // TODO - Modify select time
                    OutlinedButton(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            isTimeDialogShowing.value = true
                        }
                    ) {
                        Text(text = "Select Time", fontSize = 10.sp)
                        val time = showTimePickerDialog(context = context, isShowing = isTimeDialogShowing)
                        currentTodoTimeValue.value = time
                        isTimeDialogShowing.value = false
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                openEditDialog.value = false
                if (currentTodoDateValue.value.isNullOrEmpty()) {
                    currentTodoDateValue.value = todosList.value[selectedItem.value].date
                }
                if (currentTodoTimeValue.value.isNullOrEmpty()) {
                    currentTodoTimeValue.value = todosList.value[selectedItem.value].time
                }
                todo = Todo(
                    currentTodoID.value,
                    currentTodoTitle.value?.ifEmpty {
                        "No Name"
                    },
                    currentTodoChecked.value,
                    currentTodoDateValue.value,
                    currentTodoTimeValue.value,
                    currentTodoNotificationId.value,
                    isRecurring = false
                )
                todoViewModel.updateTodo(
                    todo
                )
                if(!currentTodoTimeValue.value.isNullOrEmpty()){
                    try {
                        scheduleNotification(
                            context = context,
                            titleText = currentTodoTitle.value,
                            messageText = "Did you complete your Task ?",
                            time = "${currentTodoDateValue.value} ${currentTodoTimeValue.value}",
                            todo = todo
                        )
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                enteredText1 = ""
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openEditDialog.value = false
                    todo = Todo(
                        currentTodoID.value,
                        currentTodoTitle.value,
                        currentTodoChecked.value,
                        currentTodoDateValue.value,
                        currentTodoTimeValue.value,
                        currentTodoNotificationId.value,
                        false
                    )
                    todoViewModel.deleteTodo(
                        todo
                    )
                    cancelNotification(
                        context = context,
                        titleText = currentTodoTitle.value,
                        messageText = "Did you complete your Task ?",
                        time = "${currentTodoDateValue.value} ${currentTodoTimeValue.value}",
                        todo = todo
                    )
                    enteredText1 = ""
                    todoViewModel.playDeletedSound(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        0xFFF1574C
                    ), contentColor = Color.White
                )
            ) {
                Text(text = "Delete")
            }
        }
    )
}