package com.thatsmanmeet.tasky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.tasky.room.Todo
import com.thatsmanmeet.tasky.room.TodoViewModel

@Composable
fun TodoItemCard(
    todo: Todo,
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
){
    val currentCheckBoxState = remember {
        mutableStateOf(todo.isCompleted)
    }
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .heightIn(min = 60.dp)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = currentCheckBoxState.value,
                onCheckedChange = {
                    currentCheckBoxState.value = it
                    viewModel.updateTodo(Todo(
                        todo.ID,
                        todo.title,
                        currentCheckBoxState.value,
                        todo.date,
                        todo.time
                    ))
                    if(currentCheckBoxState.value){
                        viewModel.playCompletedSound(context)
                    }
                })
            Spacer(modifier = Modifier.width(3.dp))
            Text(modifier = Modifier.fillMaxWidth(0.9f),
                text = todo.title!!,
                textDecoration = if (currentCheckBoxState.value) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = 16.sp
            )
        }
        if(todo.time!!.isNotEmpty()){
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
