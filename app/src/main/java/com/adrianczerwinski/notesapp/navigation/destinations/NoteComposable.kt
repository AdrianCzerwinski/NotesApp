package com.adrianczerwinski.notesapp.navigation.destinations

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.Constants.NOTE_ARGUMENT_KEY
import com.adrianczerwinski.notesapp.data.util.Constants.NOTE_SCREEN
import com.adrianczerwinski.notesapp.ui.screens.note.NoteScreen
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel

fun NavGraphBuilder.noteComposable(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    composable(
        route = NOTE_SCREEN,
        arguments = listOf(navArgument(NOTE_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){ navBackStackEntry ->
        val noteId = navBackStackEntry.arguments!!.getInt(NOTE_ARGUMENT_KEY)
        LaunchedEffect(key1 = noteId) {
            sharedViewModel.getSelectedNote(noteId = noteId)
        }
        val selectedNote by sharedViewModel.selectedNote.collectAsState()
        
        LaunchedEffect(key1 = selectedNote){
            if(selectedNote != null || noteId == -1) {
                sharedViewModel.updateNoteFields(selectedNote = selectedNote)
            }

        }
        
        NoteScreen(
            navigateToListScreen = navigateToListScreen,
            selectedNote = selectedNote,
            sharedViewModel = sharedViewModel
        )




    }
}