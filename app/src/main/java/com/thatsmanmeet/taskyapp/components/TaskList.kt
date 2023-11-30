package com.thatsmanmeet.taskyapp.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoViewModel
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
    list : List<Todo>,
    todoViewModel: TodoViewModel,
    onClick : (Int) -> Unit,
    searchText: String
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    //Filter list for search operation.
    val regex =  Regex(searchText, RegexOption.IGNORE_CASE)
    val searchedList = if(searchText.isEmpty()) list
                        else list.filter {
                            regex.containsMatchIn(it.title.toString())
                                    || regex.containsMatchIn(it.todoDescription.toString())
                        }

    val grouped = searchedList.groupBy {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.date!!)
    }.entries.sortedByDescending { it.key }
    val isSwipeDeleteDialogShowing = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LazyColumn(
        state = state,
        modifier = modifier.padding(16.dp)
    ){
        grouped.forEach { (date, groupedList) ->
            stickyHeader(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date!!)) {
                val currentDate = date.toString().split(" ")
                val month = currentDate[1]
                val dateOfMonth = currentDate[2]
                val year = currentDate[5]
                DateHeader(date = "$month $dateOfMonth, $year")
            }
            itemsIndexed(groupedList){_,item->
                val movableContent = movableContentOf {
                    val currentItem by rememberUpdatedState(item)
                    val dismissState = rememberDismissState()


                    if(dismissState.isDismissed(direction = DismissDirection.EndToStart)){
                        isSwipeDeleteDialogShowing.value = true
                        ActionDialogBox(
                            isDialogShowing = isSwipeDeleteDialogShowing,
                            title = "Delete Task?",
                            message = "Do you want to delete this task?",
                            confirmButtonText = "Delete",
                            dismissButtonText = "Cancel",
                            onConfirmClick = {
                                todoViewModel.deleteTodo(currentItem)
                            },
                            onDismissClick = {
                                isSwipeDeleteDialogShowing.value = false
                                coroutineScope.launch {
                                    dismissState.reset()
                                }
                            },
                            confirmButtonColor = Color(0xFFF75F5F),
                            confirmButtonContentColor = Color.White
                            )
                    }

                    if(dismissState.isDismissed(direction = DismissDirection.StartToEnd)){
                        todoViewModel.updateTodo(currentItem.copy(isCompleted = !currentItem.isCompleted))
                        if(!currentItem.isCompleted){
                            cancelNotification(LocalContext.current,currentItem)
                        }else{
                            CurrentDateTimeComparator(
                                inputDate = currentItem.date!!,
                                inputTime = currentItem.time!!) {
                                scheduleNotification(
                                    context = context,
                                    titleText = currentItem.title,
                                    messageText = currentItem.todoDescription,
                                    time = "${currentItem.date} ${currentItem.time}",
                                    todo = currentItem
                                )
                            }
                        }
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                            // background color
                            val backgroundColor by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.DismissedToStart -> Color(0xFFF44336)
                                    DismissValue.DismissedToEnd -> Color(0xFF4CAF50)
                                    else -> MaterialTheme.colorScheme.inverseOnSurface
                                }, label = ""
                            )
                            // icon
                            val iconImageVector = when (dismissState.targetValue) {
                                DismissValue.DismissedToEnd -> Icons.Outlined.Check
                                else -> Icons.Outlined.Delete
                            }

                            // icon placement
                            val iconAlignment = when (dismissState.targetValue) {
                                DismissValue.DismissedToEnd -> Alignment.CenterStart
                                else -> Alignment.CenterEnd
                            }

                            // icon size
                            val iconScale by animateFloatAsState(
                                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.5f else 1.3f,
                                label = ""
                            )
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(backgroundColor)
                                    .padding(start = 16.dp, end = 16.dp),
                                contentAlignment = iconAlignment
                            ){
                                Icon(
                                    modifier = Modifier.scale(iconScale),
                                    imageVector = iconImageVector,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        dismissContent = {
                            TodoItemCard(
                                todo = item ,
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