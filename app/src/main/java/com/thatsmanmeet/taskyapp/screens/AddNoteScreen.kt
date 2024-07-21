package com.thatsmanmeet.taskyapp.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.notes.Note
import com.thatsmanmeet.taskyapp.room.notes.NoteViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as Activity
    val notesViewModel = NoteViewModel(activity.application)
    val settingsStore = SettingsStore(context)
    val savedThemeKey = settingsStore.getThemeModeKey.collectAsState(initial = "0")
    val savedFontKey = settingsStore.getUseSystemFontKey.collectAsState(initial = false)

    val titleText = remember { mutableStateOf("") }
    val bodyText = remember { mutableStateOf("") }
    val isPinned = remember { mutableStateOf(false) }
    val isSavedByClick = remember { mutableStateOf(false) }

    fun getCurrentDate(): String {
        val zoneId = ZoneId.systemDefault()
        val zoneDateTime = ZonedDateTime.now(zoneId)
        return zoneDateTime.toLocalDate().toString()
    }

    TaskyTheme(
        darkTheme = when (savedThemeKey.value.toString()) {
            "null", "0" -> isSystemInDarkTheme()
            "1" -> false
            else -> true
        },
        useSystemFont = savedFontKey.value!!
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "New Note") },
                    actions = {
                        IconButton(onClick = { isPinned.value = !isPinned.value }) {
                            if (!isPinned.value) {
                                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color.Red)
                            }
                        }
                        if(titleText.value != "" || bodyText.value != ""){
                            IconButton(onClick = {
                                isSavedByClick.value = true
                                notesViewModel.upsertNote(
                                    Note(
                                        title = titleText.value,
                                        body = bodyText.value,
                                        isFavourite = isPinned.value,
                                        date = getCurrentDate()
                                    )
                                ).also {
                                    navHostController.navigate(Screen.NotesScreen.route) {
                                        popUpTo(Screen.NotesScreen.route) { inclusive = true }
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Done, contentDescription = "",tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                    ,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = modifier.fillMaxSize()) {
                    TextField(
                        modifier = modifier.fillMaxWidth(),
                        value = titleText.value,
                        onValueChange = { titleText.value = it },
                        placeholder = { Text(text = "Title") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = modifier.height(5.dp))
                    TextField(
                        modifier = modifier.fillMaxSize(),
                        value = bodyText.value,
                        onValueChange = { bodyText.value = it },
                        placeholder = { Text(text = "Write Something...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!isSavedByClick.value && (titleText.value != "")) {
                notesViewModel.upsertNote(
                    Note(
                        title = titleText.value,
                        body = bodyText.value,
                        isFavourite = isPinned.value,
                        date = getCurrentDate()
                    )
                )
            }
        }
    }
}
