package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list : MutableList<Todo>,
    todoViewModel: TodoViewModel,
    onClick : (Int) -> Unit
) {
    LazyColumn(
        state = state,
        modifier = modifier.padding(16.dp)
    ){
        itemsIndexed(list) { index, item ->
            val movableContent = movableContentOf {
                TodoItemCard(
                    todo = item,
                    viewModel = todoViewModel,
                    modifier = Modifier
                        .clickable {
                            onClick(index)
                        })
            }
            movableContent()
        }
    }
}