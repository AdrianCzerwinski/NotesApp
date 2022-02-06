package com.adrianczerwinski.notesapp.ui.screens.note

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun NoteScreen(
    selectedNote: NoteTask?,
    navigateToListScreen:(Action) -> Unit,
    sharedViewModel: SharedViewModel
){
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    Scaffold(
        topBar = {
                 NoteAppBar(
                     selectedNote = selectedNote,
                     navigateToListScreen = navigateToListScreen)
        },
        content = {
            NoteContent(
                title = title,
                onTitleChange = {sharedViewModel.updateTitle(it)},
                description = description,
                onDescriptionChange = {sharedViewModel.description.value = it},
                priority = priority,
                onPrioritySelected = {sharedViewModel.priority.value = it}
            )
        }
    )
}