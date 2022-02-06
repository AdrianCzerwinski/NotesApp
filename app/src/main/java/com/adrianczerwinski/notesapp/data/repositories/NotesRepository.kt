package com.adrianczerwinski.notesapp.data.repositories

import com.adrianczerwinski.notesapp.data.NotesDao
import com.adrianczerwinski.notesapp.data.models.NoteTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class NotesRepository @Inject constructor(private val notesDao: NotesDao) {

    val getAllNotes: Flow<List<NoteTask>> = notesDao.getAllNotes()
    val sortByLowPriority: Flow<List<NoteTask>> = notesDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<NoteTask>> = notesDao.sortByHighPriority()

    fun getSelectedNote(noteId: Int): Flow<NoteTask> {
        return notesDao.getSelectedNote(noteId)
    }

    suspend fun addNote(noteTask: NoteTask) {
        notesDao.addNote(noteTask)
    }

    suspend fun updateNote(noteTask: NoteTask) {
        notesDao.updateNote(noteTask = noteTask)
    }

    suspend fun deleteNote(noteTask: NoteTask) {
        notesDao.delete(noteTask = noteTask)
    }

    suspend fun deleteAll() {
        notesDao.deleteAllNotes()
    }

    fun searchDatabase(searchQuery: String): Flow<List<NoteTask>> {
        return notesDao.searchDatabase(searchQuery)
    }





}