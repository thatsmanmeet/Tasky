package com.thatsmanmeet.tasky

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.thatsmanmeet.tasky.room.Todo
import com.thatsmanmeet.tasky.room.TodoViewModel
import com.thatsmanmeet.tasky.ui.theme.TaskyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val todoViewModel = TodoViewModel(activity.application)
    val listState = rememberLazyListState()
    val selectedItem = remember {
        mutableStateOf(0)
    }
    val todosList = todoViewModel.getAllTodos.observeAsState(initial = listOf())
    val topAppBarColors = TopAppBarDefaults
    val openDialog = remember {
        mutableStateOf(false)
    }
    val openEditDialog = remember {
        mutableStateOf(false)
    }
    var enteredText by remember {
        mutableStateOf("")
    }
    TaskyTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name))},
                    colors = topAppBarColors.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Add Task") },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    onClick = {
                        openDialog.value = true
                    })
            }
        ) { paddingValues ->
            if(openDialog.value){
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = { Text(text = "Add Task")},
                    text = {
                        OutlinedTextField(
                            value = enteredText,
                            placeholder = { Text(text = "what's on your mind?")},
                            onValueChange = {textChange ->
                                enteredText = textChange
                            } )
                    },
                    confirmButton = {
                        Button(onClick = {
                            openDialog.value = false
                            todoViewModel.insertTodo(
                                Todo(null,enteredText,false)
                            )
                            enteredText = ""
                        }) {
                            Text(text = "Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            openDialog.value = false
                            enteredText = ""
                        }) {
                            Text(text = "Cancel")
                        }
                    })
            }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if(todosList.value.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = "No Tasks",
                            fontSize = 30.sp)
                    }
                }else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        itemsIndexed(todosList.value) { index, item ->
                            val movableContent = movableContentOf {
                                TodoItemCard(
                                    todo = item,
                                    viewModel = todoViewModel,
                                    modifier = Modifier
                                        .clickable {
                                            selectedItem.value = index
                                            openEditDialog.value = true
                                        })
                            }
                            movableContent()
                        }
                    }
                }
                // LazyColumn ends here
                if (openEditDialog.value){
                    val currentTodoTitle = remember {
                        mutableStateOf(todosList.value[selectedItem.value].title)
                    }
                    val currentTodoID = remember {
                        mutableStateOf(todosList.value[selectedItem.value].ID)
                    }

                    val currentTodoChecked = remember {
                        mutableStateOf(todosList.value[selectedItem.value].isCompleted)
                    }
                    AlertDialog(
                        onDismissRequest = {
                            openEditDialog.value = false
                        },
                        title = { Text(text = "Edit Task")},
                        text = {
                            OutlinedTextField(
                                value = currentTodoTitle.value!!,
                                placeholder = { Text(text = "what do you want to accomplish ?")},
                                onValueChange = {textChange ->
                                    currentTodoTitle.value = textChange
                                } )
                        },
                        confirmButton = {
                            Button(onClick = {
                                openEditDialog.value = false
                                todoViewModel.updateTodo(
                                    Todo(currentTodoID.value,currentTodoTitle.value,currentTodoChecked.value)
                                )
                                enteredText = ""
                            }) {
                                Text(text = "Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                openEditDialog.value = false
                                todoViewModel.deleteTodo(
                                    Todo(currentTodoID.value,currentTodoTitle.value,currentTodoChecked.value)
                                )
                                enteredText = ""
                                todoViewModel.playDeletedSound(context)
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFFF1574C
                                ), contentColor = Color.White)
                            ) {
                                Text(text = "Delete")
                            }
                        })
                }
            }

        }
    }
}
