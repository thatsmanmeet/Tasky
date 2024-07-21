package com.thatsmanmeet.taskyapp.screens


import android.app.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.components.MyTopAppBar
import com.thatsmanmeet.taskyapp.components.NavigationDrawer
import com.thatsmanmeet.taskyapp.components.NotesHeader
import com.thatsmanmeet.taskyapp.components.NotesItem
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.notes.NoteViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val notesViewModel = NoteViewModel(activity.application)

    val notesListFlow by notesViewModel.getAllNotesFlow.collectAsState(initial = emptyList())

    val listState = rememberLazyListState()

    val topAppBarColors = TopAppBarDefaults

    val searchText = rememberSaveable {
        mutableStateOf("")
    }
    val regex = Regex(searchText.value, RegexOption.IGNORE_CASE)
    // Group and sort notes

    val searchList = if(searchText.value.isEmpty()) {
        notesListFlow
    } else {
        notesListFlow.filter {
            regex.containsMatchIn(it.title.toString()) || regex.containsMatchIn(it.body.toString())
        }
    }

    val groupedList = searchList.groupBy { it.isFavourite }.toSortedMap(compareByDescending { it })

    val sortedGroupedNotes = groupedList.mapValues { (_, notes) ->
        notes.sortedByDescending { it.date }
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // setup settings store
    val settingsStore = SettingsStore(context)
    val savedThemeKey = settingsStore.getThemeModeKey.collectAsState(initial = "0")
    val savedFontKey = settingsStore.getUseSystemFontKey.collectAsState(initial = false)
    TaskyTheme(darkTheme = when (savedThemeKey.value.toString()) {
        "null" -> {isSystemInDarkTheme()}
        "0" -> {isSystemInDarkTheme()}
        "1" -> {false}
        else -> {true}
    },
        useSystemFont = savedFontKey.value!!
    ) {
        NavigationDrawer(navHostController = navHostController, coroutineScope = coroutineScope, drawerState = drawerState) {
            Scaffold(
                snackbarHost = {SnackbarHost(hostState = snackBarHostState)},
                topBar = {
                    MyTopAppBar(title = "Notes", coroutineScope, drawerState, modifier, searchText, topAppBarColors)
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Add Note") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_add_task), contentDescription = null) },
                        onClick = {
                            navHostController.navigate(Screen.AddNotesScreen.route)
                        },
                        expanded = listState.isScrollingUp()
                    )
                }
            ) { paddingValues ->
                Surface(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(notesListFlow.isEmpty()){
                        Box(modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                            contentAlignment = Alignment.Center) {
                            Column(
                                modifier = modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = modifier
                                        .size(50.dp)
                                        .alpha(0.8f),
                                    painter = painterResource(id = R.drawable.ic_notes),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "No Notes",
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }
                    }else {
                        LazyColumn(
                            state = listState,
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            sortedGroupedNotes.forEach { (fav, notes) ->
                                stickyHeader {
                                    if (fav!!) {
                                        NotesHeader(text = "Favourites") {
                                            Icon(
                                                imageVector = Icons.Filled.Favorite,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        }
                                    } else {
                                        NotesHeader(text = "Notes") {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_notes),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }

                                items(notes) { note ->
                                    NotesItem(note = note, navHostController = navHostController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Preview
@Composable
fun DisplayNoteScreen() {
    NotesScreen(navHostController = rememberNavController())
}