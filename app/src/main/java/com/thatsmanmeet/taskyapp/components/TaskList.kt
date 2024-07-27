package com.thatsmanmeet.taskyapp.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodo
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodoViewModel
import com.thatsmanmeet.taskyapp.screens.CurrentDateTimeComparator
import com.thatsmanmeet.taskyapp.screens.cancelNotification
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list: List<Todo>,
    todoViewModel: TodoViewModel,
    deletedTodoViewModel: DeletedTodoViewModel,
    onClick: (Int) -> Unit,
    searchText: String,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    // Filter list for search operation.
    val regex = Regex(searchText, RegexOption.IGNORE_CASE)
    val searchedList = if (searchText.isEmpty()) list
    else list.filter {
        regex.containsMatchIn(it.title.toString())
                || regex.containsMatchIn(it.todoDescription.toString())
    }

    val grouped = searchedList.groupBy {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.date!!)
    }.entries.sortedByDescending { it.key }

    LazyColumn(
        state = state,
        modifier = modifier.padding(16.dp)
    ) {
        grouped.forEach { (date, groupedList) ->
            stickyHeader(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date!!)) {
                val currentDate = date.toString().split(" ")
                val month = currentDate[1]
                val dateOfMonth = currentDate[2]
                val year = currentDate[5]
                DateHeader(date = "$month $dateOfMonth, $year")
            }
            itemsIndexed(groupedList) { _, item ->
                val movableContent = movableContentOf {
                    val currentItem by rememberUpdatedState(item)
                    val dismissState = rememberSwipeToDismissBoxState()

                    if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                        todoViewModel.updateTodo(currentItem.copy(isCompleted = !currentItem.isCompleted))
                        val currentCompletionCondition = !currentItem.isCompleted
                        if (!currentCompletionCondition) {
                            if (currentItem.time!! != "") {
                                CurrentDateTimeComparator(
                                    inputDate = currentItem.date!!,
                                    inputTime = currentItem.time!!
                                ) {
                                    scheduleNotification(
                                        context = context,
                                        titleText = currentItem.title,
                                        messageText = currentItem.todoDescription,
                                        time = "${currentItem.date} ${currentItem.time}",
                                        todo = currentItem
                                    )
                                }
                            }
                        } else {
                            // cancel notification
                            cancelNotification(context, currentItem)
                        }
                    } else if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                       LaunchedEffect(Unit) {
                           coroutineScope.launch {
                               deletedTodoViewModel.insertDeletedTodo(
                                   DeletedTodo(
                                       ID = currentItem.ID,
                                       title = currentItem.title,
                                       todoDescription = currentItem.todoDescription,
                                       isCompleted = currentItem.isCompleted,
                                       date = currentItem.date,
                                       time = currentItem.time,
                                       isRecurring = currentItem.isRecurring,
                                       notificationID = currentItem.notificationID,
                                       todoDeletionDate = getDate30DaysLater(currentItem.date!!)
                                   )
                               )
                               todoViewModel.deleteTodo(currentItem)
                               cancelNotification(context, currentItem)
                           }

                       }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            // background color
                            val backgroundColor by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336)
                                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50)
                                    else -> MaterialTheme.colorScheme.inverseOnSurface
                                }, label = ""
                            )
                            // icon
                            val iconImageVector = when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.StartToEnd -> Icons.Outlined.Check
                                else -> Icons.Outlined.Delete
                            }

                            // icon placement
                            val iconAlignment = when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                                else -> Alignment.CenterEnd
                            }

                            // icon size
                            val iconScale by animateFloatAsState(
                                targetValue = 1f,
                                label = ""
                            )
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(backgroundColor)
                                    .padding(start = 16.dp, end = 16.dp),
                                contentAlignment = iconAlignment
                            ) {
                                Icon(
                                    modifier = Modifier.scale(iconScale),
                                    imageVector = iconImageVector,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        content = {
                            TodoItemCard(
                                todo = item,
                                viewModel = todoViewModel,
                                modifier = modifier.clickable {
                                    onClick(list.indexOf(item))
                                }
                            )
                        }
                    )
                    Spacer(modifier = modifier.height(12.dp))
                }
                movableContent()
            }
        }
    }
}

fun getDate30DaysLater(enteredDate: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = sdf.parse(enteredDate)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    calendar.add(Calendar.DAY_OF_MONTH, 30)
    return sdf.format(calendar.time)
}
