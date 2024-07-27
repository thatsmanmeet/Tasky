package com.thatsmanmeet.taskyapp.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTimePicker(
    context: Context,
    isShowing: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    is24HourField:Boolean,
    onTimeSelected: (String) -> Unit
) {
    val currentTime = LocalTime.now()
    val timePickerState = rememberTimePickerState(
            is24Hour = is24HourField  ,
            initialHour = currentTime.hour,
            initialMinute = currentTime.minute
        )


    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    if (isShowing.value) {
        AlertDialog(
            onDismissRequest = {
                isShowing.value = false
            },
            title = { Text(text = "Select Time") },
            text = {
                TimePicker(
                    state = timePickerState,
                    modifier = modifier
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isShowing.value = false
                        val selectedHour = timePickerState.hour
                        val selectedMinute = timePickerState.minute
                        val time = LocalTime.of(selectedHour, selectedMinute)
                        val formattedTime = time.format(timeFormatter)
                        onTimeSelected(formattedTime)
                    },
                ) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isShowing.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}
