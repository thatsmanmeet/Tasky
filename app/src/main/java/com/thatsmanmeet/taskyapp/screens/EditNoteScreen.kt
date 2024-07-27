package com.thatsmanmeet.taskyapp.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.room.notes.Note
import com.thatsmanmeet.taskyapp.room.notes.NoteViewModel
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    navHostController: NavHostController,
    noteID: Long?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as Activity
    val notesViewModel = NoteViewModel(activity.application)
    val currentNote = notesViewModel.getOneNote(noteID!!)
    val settingsStore = SettingsStore(context)
    val savedThemeKey = settingsStore.getThemeModeKey.collectAsState(initial = "0")
    val savedFontKey = settingsStore.getUseSystemFontKey.collectAsState(initial = false)

    val titleText = remember {
        mutableStateOf(currentNote.title)
    }

    val bodyText = remember{
        mutableStateOf(currentNote.body)
    }

    val isPinned = remember {
        mutableStateOf(currentNote.isFavourite!!)
    }

    val isSavingIndicator = remember {
        mutableStateOf(false)
    }

    var isChangeOccur by remember {
        mutableStateOf(false)
    }

    val isDeleteDialogVisible = remember {
        mutableStateOf(false)
    }


    fun getCurrentDate(): String{
        val zoneId = ZoneId.systemDefault()
        val zoneDateTime = ZonedDateTime.now(zoneId)
        return zoneDateTime.toLocalDate().toString()
    }


    TaskyTheme(darkTheme = when (savedThemeKey.value.toString()) {
        "null" -> {
            isSystemInDarkTheme()
        }
        "0" -> {
            isSystemInDarkTheme()
        }
        "1" -> {false}
        else -> {true}
    },
        useSystemFont = savedFontKey.value!!
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = if(titleText.value.isNullOrEmpty()) {"Empty Note"} else {titleText.value!!}, maxLines = 1)},
                    actions = {
                        IconButton(onClick = {
                            isChangeOccur = true
                            isSavingIndicator.value = true
                            isPinned.value = !isPinned.value
                        }) {
                            if(!isPinned.value){
                                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "",tint = MaterialTheme.colorScheme.onPrimary)
                            }else{
                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color.Red)
                            }
                        }

                        IconButton(onClick = {
                            isDeleteDialogVisible.value = true
                        }) {
                            Icon(imageVector = Icons.Filled.Delete , contentDescription = "", tint = Color.Red)
                            if(isDeleteDialogVisible.value){
                                AlertDialog(
                                    onDismissRequest = {
                                        isDeleteDialogVisible.value = false
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {
                                            isDeleteDialogVisible.value = false
                                        }) {
                                            Text(text = "Cancel")
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            isDeleteDialogVisible.value = false
                                            notesViewModel.deleteNote(Note(
                                                ID = currentNote.ID,
                                                title = currentNote.title,
                                                body = currentNote.body,
                                                isFavourite = currentNote.isFavourite,
                                                date = currentNote.date
                                            )).also {
                                                navHostController.navigate(Screen.NotesScreen.route) {
                                                    popUpTo(Screen.NotesScreen.route) { inclusive = true }
                                                }
                                            }
                                        }) {
                                            Text(text = "Delete")
                                        }
                                    },
                                    title = { Text(text = "Delete Note")},
                                    text = { Text(text = "Would you like to delete this note?")
                                    }
                                )
                            }
                        }

                        if(isSavingIndicator.value){
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = modifier.size(24.dp).padding(end = 2.dp)
                            )
                        }else{
                            Icon(imageVector = Icons.Default.Check, contentDescription = "",tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier.padding(end = 2.dp))
                        }

                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) {paddingValues->
                LaunchedEffect(key1 = titleText.value, key2 = bodyText.value, key3 = isPinned.value) {
                    if(isChangeOccur){
                        notesViewModel.upsertNote(
                            Note(
                                ID = noteID,
                                title = titleText.value,
                                body = bodyText.value,
                                isFavourite = isPinned.value,
                                date = getCurrentDate()
                            )
                        ).also {
                            delay(500)
                            isSavingIndicator.value = false
                        }
                    }
                }

            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.background
            ) {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    TextField(
                        modifier = modifier.fillMaxWidth(),
                        value = titleText.value!!,
                        onValueChange = {
                            isChangeOccur = true
                            titleText.value = it
                            isSavingIndicator.value = true
                        },
                        placeholder = { Text(text = "Title")},
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
                        value = bodyText.value!!,
                        onValueChange = {
                            isChangeOccur = true
                            bodyText.value = it
                            isSavingIndicator.value = true
                        },
                        placeholder = { Text(text = "Write Something...")},
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
            if (bodyText.value != currentNote.body || titleText.value != currentNote.title || isPinned.value != currentNote.isFavourite) {
                notesViewModel.upsertNote(
                    Note(
                        ID = noteID,
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
