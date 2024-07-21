package com.thatsmanmeet.taskyapp.room.notes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : ViewModel() {
    private var repository:NoteRepository
    val getAllNotesFlow: Flow<List<Note>>

    init{
        val dao = NoteDatabase.getInstance(application).noteDao()
        repository = NoteRepository(dao)
        getAllNotesFlow = repository.getAllNotes()
    }

    fun getOneNote(id:Long) : Note{
        return repository.getOneNote(id)
    }

    fun upsertNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

}