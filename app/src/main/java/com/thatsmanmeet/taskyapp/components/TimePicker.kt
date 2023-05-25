package com.thatsmanmeet.taskyapp.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import java.util.*

@Composable
fun showTimePickerDialog(
    context:Context,
    isShowing: MutableState<Boolean>
):String{
    val settingsStore = SettingsStore(context)
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val time = remember { mutableStateOf("") }
    val timePickerDialog = TimePickerDialog(
        context,
        {_, currentHour : Int, currentMinute: Int ->
            time.value = "$currentHour:$currentMinute"
        }, hour, minute, settingsStore.getClockKey.collectAsState(initial = false).value!!
    )
    if(isShowing.value){
        timePickerDialog.show()
    }
    return time.value
}