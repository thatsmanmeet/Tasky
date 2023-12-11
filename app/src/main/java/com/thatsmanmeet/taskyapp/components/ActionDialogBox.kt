package com.thatsmanmeet.taskyapp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ActionDialogBox(
    modifier: Modifier = Modifier,
    isDialogShowing: MutableState<Boolean>,
    title: String,
    message:String,
    confirmButtonText:String,
    dismissButtonText:String,
    onConfirmClick : () -> Unit,
    onDismissClick : () -> Unit,
    confirmButtonColor:Color = MaterialTheme.colorScheme.primary,
    confirmButtonContentColor:Color = MaterialTheme.colorScheme.onPrimary
) {
    if(isDialogShowing.value){
        AlertDialog(
            onDismissRequest = {
                isDialogShowing.value = false
            },
            confirmButton = {
                Button(onClick = {
                    onConfirmClick()
                    isDialogShowing.value = false
                }, colors = ButtonColors(containerColor = confirmButtonColor, contentColor = confirmButtonContentColor, disabledContainerColor = Color.Gray, disabledContentColor = Color.White)) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDismissClick()
                    isDialogShowing.value = false
                }) {
                    Text(text = dismissButtonText)
                }
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            }
        )
    }
}