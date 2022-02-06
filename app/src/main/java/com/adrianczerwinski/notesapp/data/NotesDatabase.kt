package com.adrianczerwinski.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adrianczerwinski.notesapp.data.models.NoteTask
import dagger.hilt.android.AndroidEntryPoint


@Database(entities = [NoteTask::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun noteDao(): NotesDao


}