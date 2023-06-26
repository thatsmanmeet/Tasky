package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list : List<Todo>,
    todoViewModel: TodoViewModel,
    onClick : (Int) -> Unit
) {
    val grouped = list.groupBy {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.date!!)
    }.entries.sortedByDescending { it.key }
    LazyColumn(
        state = state,
        modifier = modifier.padding(16.dp)
    ){
        grouped.forEach { (date, grouped_list) ->
            stickyHeader(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date!!)) {
                val currentDate = date.toString().split(" ")
                val month = currentDate[1]
                val dateOfMonth = currentDate[2]
                val year = currentDate[5]
                DateHeader(date = "$month $dateOfMonth, $year")
            }
            itemsIndexed(grouped_list){_,item->
                val movableContent = movableContentOf {
                TodoItemCard(
                    todo = item ,
                    viewModel = todoViewModel,
                    modifier = modifier.clickable {
                        onClick(list.indexOf(item))
                    }
                )
                }
                movableContent()
            }
        }
    }
}

@Composable
fun LegacyTaskList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list: List<Todo>,
    todoViewModel: TodoViewModel,
    onClick: (Int) -> Unit
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