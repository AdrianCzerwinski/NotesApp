package com.adrianczerwinski.notesapp.data

import androidx.room.*
import com.adrianczerwinski.notesapp.data.models.NoteTask
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NoteTask>>

    @Query("SELECT * FROM notes_table WHERE id=:noteId ")
    fun getSelectedNote(noteId: Int): Flow<NoteTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(noteTask: NoteTask)

    @Update
    suspend fun updateNote(noteTask: NoteTask)

    @Delete
    suspend fun delete(noteTask: NoteTask)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<NoteTask>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END ")
    fun sortByLowPriority(): Flow<List<NoteTask>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END ")
    fun sortByHighPriority(): Flow<List<NoteTask>>

}