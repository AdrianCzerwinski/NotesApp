package com.adrianczerwinski.notesapp.ui.screens.note

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun NoteScreen(
    selectedNote: NoteTask?,
    navigateToListScreen:(Action) -> Unit,
    sharedViewModel: SharedViewModel
){
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context = LocalContext.current
    
    BackHandler(onBackPressed = {navigateToListScreen(Action.NO_ACTION)})

    Scaffold(
        topBar = {
                 NoteAppBar(
                     selectedNote = selectedNote,
                     navigateToListScreen = { action ->
                         if(action == Action.NO_ACTION) {
                             navigateToListScreen(action)
                         } else {
                             if (sharedViewModel.validateFields()){
                                 navigateToListScreen(action)
                             } else {
                                 displayToast(context)
                             }
                         }

                     })
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

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields empty", Toast.LENGTH_SHORT ).show()
}

@Composable
fun BackHandler (
    backDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
){
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val backCallBack = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backDispatcher) {
        backDispatcher?.addCallback(backCallBack)
        onDispose{
            backCallBack.remove()
        }
    }

}
