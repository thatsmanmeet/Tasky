package com.thatsmanmeet.tasky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.thatsmanmeet.tasky.room.Todo
import com.thatsmanmeet.tasky.room.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = currentCheckBoxState.value,
            onCheckedChange = {
            currentCheckBoxState.value = it
            viewModel.updateTodo(Todo(todo.ID,todo.title,currentCheckBoxState.value))
            if(currentCheckBoxState.value){
                viewModel.playCompletedSound(context)
            }
        })
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = todo.title!!,
            textDecoration = if (currentCheckBoxState.value) TextDecoration.LineThrough else TextDecoration.None
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}
