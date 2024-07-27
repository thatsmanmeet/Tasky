package com.thatsmanmeet.taskyapp.components

import android.content.Context
import android.widget.Toast
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
import com.thatsmanmeet.taskyapp.screens.getCurrentDate
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import com.thatsmanmeet.taskyapp.screens.setRepeatingAlarm
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun addTodoDialog(
    openDialog: MutableState<Boolean>,
    enteredText: String,
    description:String,
    dateText: MutableState<String>,
    isDateDialogShowing: MutableState<Boolean>,
    context: Context,
    timeText: MutableState<String>,
    isTimeDialogShowing: MutableState<Boolean>,
    todoViewModel: TodoViewModel,
    isRepeatingAttribute : Boolean,
    modifier: Modifier = Modifier
): String {
    var enteredText1 by remember {
        mutableStateOf(enteredText)
    }
    var descriptionText by remember{
        mutableStateOf(description)
    }
    var isRepeating by remember {
        mutableStateOf(isRepeatingAttribute)
    }
    var timeTextState by remember {
        mutableStateOf(timeText.value)
    }
    var date  by remember {
        mutableStateOf("")
    }
    var selectTime by remember {
        mutableStateOf("")
    }
    val settingsStore = SettingsStore(context)
    val is24HoursEnabled = settingsStore.getClockKey.collectAsState(initial = true).value!!
    val isLegacyDateTimePickersEnabled = settingsStore.getUseLegacyDateTimePickers.collectAsState(
        initial = false
    ).value!!

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                //openDialog.value = false
                enteredText1 = ""
            },
            title = { Text(text = stringResource(R.string.add_task_dialog_title)) },
            text = {
                Column(modifier = modifier.heightIn(min = 240.dp)) {
                    OutlinedTextField(
                        value = enteredText1,
                        placeholder = { Text(text = stringResource(id = R.string.add_edit_text_placeholder)) },
                        onValueChange = { textChange ->
                            enteredText1 = textChange
                        },
                        maxLines = 1,
                        singleLine = true
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    OutlinedTextField(
                        value = descriptionText,
                        placeholder = { Text(text = "Description")},
                        onValueChange = {descriptionTextChange->
                            descriptionText = descriptionTextChange
                        },
                        maxLines = 4
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Text(text = stringResource(R.string.add_edit_dialog_set_reminder_title))
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
                            Text(text = dateText.value)
                        }
                        OutlinedButton(modifier = modifier.height(35.dp), onClick = {
                            isDateDialogShowing.value = true
                        }) {
                            Text(text = stringResource(R.string.add_edit_dialog_select_date_button), fontSize = 10.sp)
                            if (isLegacyDateTimePickersEnabled){
                             date = showDatePicker(context = context, isDateDialogShowing) // old date
                            }else{
                                MaterialDatePicker(isShowing = isDateDialogShowing){ selectedDate->
                                    date = selectedDate
                                }
                            }
                            dateText.value = if(date != ""){date} else {
                                getCurrentDate()
                            }
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
                            Spacer(modifier = modifier.width(8.dp))
                            Text(text = timeText.value)
                        }
                        OutlinedButton(modifier = modifier.height(35.dp), onClick = {
                            isTimeDialogShowing.value = true
                        }) {
                            Text(text = stringResource(R.string.add_edit_dialog_select_time_button), fontSize = 10.sp)
                            if(isLegacyDateTimePickersEnabled){
                                val time = showTimePickerDialog(context = context, isTimeDialogShowing)
                                timeText.value = time
                                timeTextState = time
                            }else{
                                MaterialTimePicker(context,isShowing = isTimeDialogShowing, is24HourField = is24HoursEnabled) {selectedTime->
                                    selectTime = selectedTime
                                }
                                timeText.value = selectTime
                                timeTextState = selectTime
                            }
                            isTimeDialogShowing.value = false
                        }
                    }
                    // Repeating Notifications
                    Spacer(modifier = modifier.height(5.dp))
                    if (timeTextState.isNotEmpty()) {
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
                                    Text(text = stringResource(R.string.add_edit_dialog_repeat_everyday), fontSize = 12.sp)
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
                    if (enteredText1.isNotEmpty()){
                         val todo = Todo(
                            ID = null,
                             title = enteredText1,
                            //enteredText1.ifEmpty { "No Name" },
                            isCompleted = false,
                            date = dateText.value.ifEmpty {
                                SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(
                                    Calendar.getInstance().time
                                ).toString()
                            },
                            time = timeText.value,
                            notificationID = ((0..2000).random() - (0..50).random()),
                            isRecurring = isRepeating,
                            todoDescription = descriptionText
                        )
                        todoViewModel.insertTodo(
                            todo
                        )
                        if(dateText.value.isEmpty()){
                            dateText.value = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(
                                Calendar.getInstance().time
                            ).toString()
                        }
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
                            if(calendar.timeInMillis > Calendar.getInstance().timeInMillis){
                                scheduleNotification(
                                    context,
                                    titleText = enteredText1,
                                    messageText = descriptionText,
                                    time = "${dateText.value} ${timeText.value}",
                                    todo = todo
                                )
                            }
                            if(isRepeating){
                                setRepeatingAlarm(context = context)
                            }
                        }
                        openDialog.value = false
                        enteredText1 = ""
                        descriptionText = ""
                        isRepeating = false
                        selectTime = ""
                        date = ""
                    }
                    else{
                        Toast.makeText(context,"Task name is required.",Toast.LENGTH_SHORT).show()
                    }
                }
                ) {
                    Text(text = stringResource(R.string.add_edit_dialog_add_button_text))
                }
            },
            dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                    enteredText1 = ""
                    descriptionText = ""
                    selectTime = ""
                    date = ""
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1574C),
                        contentColor = Color.White
                    )) {
                    Text(text = stringResource(R.string.add_edit_dialog_cancel_button_text))
                }
            })
         isRepeating = false
    }
    return enteredText1
}