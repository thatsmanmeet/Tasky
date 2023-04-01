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
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel

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
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Reminder")
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
                        Text(text = todosList.value[selectedItem.value].date!!)
                    }
                }
                // time
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
                        Text(text = todosList.value[selectedItem.value].time!!)
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
                enteredText1 = ""
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
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