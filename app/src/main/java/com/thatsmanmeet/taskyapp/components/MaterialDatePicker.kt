package com.thatsmanmeet.taskyapp.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDatePicker(
    isShowing: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    val currentDate = Calendar.getInstance().timeInMillis
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentDate)
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    if (isShowing.value) {
        DatePickerDialog(
            onDismissRequest = {
                isShowing.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isShowing.value = false
                        val dateResult = if (datePickerState.selectedDateMillis != null) {
                            val date = Date(datePickerState.selectedDateMillis!!)
                            dateFormat.format(date)
                        } else {
                            "No Selection"
                        }
                        onDateSelected(dateResult)

                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isShowing.value = false }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
