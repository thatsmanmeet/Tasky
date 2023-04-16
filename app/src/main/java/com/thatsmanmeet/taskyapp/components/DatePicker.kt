package com.thatsmanmeet.taskyapp.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.*

@Composable
fun showDatePicker(
    context: Context,
    isShowing : MutableState<Boolean>
) : String{
    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, currentYear: Int, currentMonth: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/${currentMonth+1}/$currentYear"
            isShowing.value = false
        }, year, month, day
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    if(isShowing.value){
        datePickerDialog.show()
    }
    return date.value
}