package com.thatsmanmeet.taskyapp.room.notes

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotesFlow()

    fun getOneNote(id:Long): Note = noteDao.getOneNote(id)

    suspend fun upsertNote(note: Note) = noteDao.upsertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)


}