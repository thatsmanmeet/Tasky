package com.thatsmanmeet.taskyapp.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodo
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodoViewModel
import com.thatsmanmeet.taskyapp.screens.cancelNotification
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import com.thatsmanmeet.taskyapp.screens.setRepeatingAlarm
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("AutoboxingStateValueProperty")
@Composable
fun OpenEditTodoDialog(
    todosList: List<Todo>,
    selectedItem: MutableState<Int>,
    openEditDialog: MutableState<Boolean>,
    todoViewModel: TodoViewModel,
    deletedTodoViewModel: DeletedTodoViewModel,
    enteredText: String,
    descriptionText:String,
    context: Context,
    modifier: Modifier = Modifier,
    onDeleteAction: () -> Unit
) {
    var enteredText1 by remember {
        mutableStateOf(enteredText)
    }
    var currentDescriptionText by remember{
        mutableStateOf(descriptionText)
    }
    val currentTodoTitle = remember {
        mutableStateOf(todosList[selectedItem.value].title)
    }
    val currentTodoID = remember {
        mutableStateOf(todosList[selectedItem.value].ID)
    }

    val currentTodoChecked = remember {
        mutableStateOf(todosList[selectedItem.value].isCompleted)
    }

    val currentTodoDateValue = remember {
        mutableStateOf(todosList[selectedItem.value].date)
    }

    val currentTodoTimeValue = remember {
        mutableStateOf(todosList[selectedItem.value].time)
    }

    val currentTodoNotificationId = remember {
        mutableIntStateOf(todosList[selectedItem.value].notificationID)
    }

    val isDateDialogShowing = remember {
        mutableStateOf(false)
    }

    val isTimeDialogShowing = remember {
        mutableStateOf(false)
    }
    var isRepeating by remember {
        mutableStateOf(todosList[selectedItem.value].isRecurring)
    }
    var timeTextState by remember {
        mutableStateOf(currentTodoTimeValue.value)
    }
    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    val settingsStore = SettingsStore(context = context)
    val savedSoundKey = settingsStore.getSoundKey.collectAsState(initial = true)
    val is24HoursEnabled = settingsStore.getClockKey.collectAsState(initial = true).value!!
    val isLegacyDateTimePickersEnabled = settingsStore.getUseLegacyDateTimePickers.collectAsState(
        initial = false
    ).value!!
    var date = "";
    var selectedDateData by remember {
        mutableStateOf("")
    }
    var selectedTimeData by remember {
        mutableStateOf("")
    }
    var todo : Todo
    var deletedTodo: DeletedTodo
    AlertDialog(
        onDismissRequest = {
            openEditDialog.value = false
        },
        title = { Text(text = stringResource(R.string.edit_task_dialog_title)) },
        text = {
            Column(modifier = modifier.heightIn(min=240.dp)) {
                OutlinedTextField(
                    value = currentTodoTitle.value!!,
                    placeholder = { Text(text = stringResource(id = R.string.add_edit_text_placeholder)) },
                    onValueChange = { textChange ->
                        currentTodoTitle.value = textChange
                    },
                    maxLines = 1,
                    singleLine = true
                )
                Spacer(modifier = modifier.height(10.dp))
                OutlinedTextField(
                    value = currentDescriptionText,
                    placeholder = { Text(text = "Description")},
                    onValueChange = {descriptionTextChange->
                        currentDescriptionText = descriptionTextChange
                },
                    maxLines = 4
                    )
                Spacer(modifier = modifier.height(12.dp))
                Text(text = stringResource(R.string.add_edit_dialog_edit_reminder_title))
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
                        Spacer(modifier = modifier.width(8.dp))
                        if(currentTodoDateValue.value.isNullOrEmpty()){
                            Text(text = todosList[selectedItem.value].date!!)
                        }else{
                            Text(text = currentTodoDateValue.value!!)
                        }
                    }
                    // Modify select date
                    OutlinedButton(modifier = modifier.height(35.dp), onClick = {
                        isDateDialogShowing.value = true
                    }) {
                        Text(text = stringResource(id = R.string.add_edit_dialog_select_date_button), fontSize = 10.sp)
                        if(isLegacyDateTimePickersEnabled){
                            date = showDatePicker(context = context, isShowing = isDateDialogShowing)
                            currentTodoDateValue.value = date
                            isTimeDialogShowing.value = false
                        }else{
                            MaterialDatePicker(isShowing = isDateDialogShowing) {selected->
                                selectedDateData = selected
                            }
                            currentTodoDateValue.value = selectedDateData
                            isTimeDialogShowing.value = false
                        }

                    }
                }
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
                        Spacer(modifier = modifier.width(8.dp))
                        if(currentTodoTimeValue.value.isNullOrEmpty()){
                            Text(text = todosList[selectedItem.value].time!!)
                        }else{
                            Text(text = currentTodoTimeValue.value!!)
                        }
                    }
                    //Modify select time
                    OutlinedButton(
                        modifier = modifier.height(35.dp),
                        onClick = {
                            isTimeDialogShowing.value = true
                        }
                    ) {
                        Text(text = stringResource(id = R.string.add_edit_dialog_select_time_button), fontSize = 10.sp)
                        if(isLegacyDateTimePickersEnabled){
                            val time = showTimePickerDialog(context = context, isShowing = isTimeDialogShowing)
                            currentTodoTimeValue.value = time
                            if(time.isNotEmpty()){
                                timeTextState = time
                            }
                        }else{
                            MaterialTimePicker(context = context, isShowing = isTimeDialogShowing, is24HourField = is24HoursEnabled ) {selectedTime->
                                selectedTimeData = selectedTime
                            }
                            currentTodoTimeValue.value = selectedTimeData
                            if(selectedTimeData.isNotEmpty()){
                                timeTextState = selectedTimeData
                            }
                        }
                        isTimeDialogShowing.value = false
                    }
                }
                // Repeating Notifications
                if (!timeTextState.isNullOrEmpty()) {
                    Box(modifier = modifier.fillMaxWidth()) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier.weight(0.8f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = modifier.width(8.dp))
                                Text(text = stringResource(id = R.string.add_edit_dialog_repeat_everyday), fontSize = 12.sp)
                            }
                            Checkbox(checked = isRepeating, onCheckedChange = {
                                isRepeating = it
                            }, modifier = modifier.weight(0.2f))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                openEditDialog.value = false
                if (currentTodoDateValue.value.isNullOrEmpty()) {
                    currentTodoDateValue.value = todosList[selectedItem.value].date
                }
                if (currentTodoTimeValue.value.isNullOrEmpty()) {
                    currentTodoTimeValue.value = todosList[selectedItem.value].time
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
                    isRecurring = isRepeating,
                    currentDescriptionText
                )
                todoViewModel.updateTodo(
                    todo
                )
                if(!currentTodoTimeValue.value.isNullOrEmpty()){
                    try {
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDate = format.parse(currentTodoDateValue.value!!)
                        val calendar = Calendar.getInstance().apply {
                            time = parsedDate!!
                            set(Calendar.HOUR_OF_DAY, todo.time!!.substringBefore(":").toInt())
                            set(Calendar.MINUTE, todo.time!!.substringAfter(":").toInt())
                            set(Calendar.SECOND, 0)
                        }
                        val currentTime = Calendar.getInstance().timeInMillis
                        if(!currentTodoChecked.value && (calendar >= currentDate && calendar.timeInMillis >= currentTime)){
                            scheduleNotification(
                                context = context,
                                titleText = currentTodoTitle.value,
                                messageText = currentDescriptionText,
                                time = "${currentTodoDateValue.value} ${currentTodoTimeValue.value}",
                                todo = todo
                            )
                        }
                        if(isRepeating){
                            setRepeatingAlarm(context)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                enteredText1 = ""
                currentDescriptionText = ""
                selectedDateData = ""
                selectedTimeData = ""
            }) {
                Text(text = stringResource(R.string.add_edit_dialog_save_button_text))
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
                        isRepeating,
                        currentDescriptionText
                    )
                    todoViewModel.deleteTodo(
                        todo
                    ).also {
                        val currentTodo = todoViewModel.getOneTodo(currentTodoID.value!!)
                        deletedTodo = DeletedTodo(
                            currentTodo.ID,
                            currentTodo.title,
                            currentTodo.isCompleted,
                            currentTodo.date,
                            currentTodo.time,
                            currentTodo.notificationID,
                            currentTodo.isRecurring,
                            currentTodo.todoDescription,
                            ""
                        )
                        deletedTodoViewModel.insertDeletedTodo(deletedTodo)
                    }
                    cancelNotification(
                        context = context,
                        todo = todo
                    )
                    enteredText1 = ""
                    currentDescriptionText = ""
                    if(savedSoundKey.value == true){
                        todoViewModel.playDeletedSound(context)
                    }
                    onDeleteAction()
                    selectedDateData = ""
                    selectedTimeData = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        0xFFF1574C
                    ), contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.add_edit_dialog_delete_button_text))
            }
        }
    )
}