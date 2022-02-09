package com.adrianczerwinski.notesapp.ui.screens.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.adrianczerwinski.notesapp.R
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.RequestState
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import com.adrianczerwinski.notesapp.ui.theme.fabBackgroundColor
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListScreen(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
    action: Action
) {

    LaunchedEffect(key1 = action ) {
        sharedViewModel.handleDatabaseActions(action)
    }

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val allNotes by sharedViewModel.allNotes.collectAsState()
    val searchedNotes by sharedViewModel.searchedNotes.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityNotes by sharedViewModel.lowPriorityNotes.collectAsState()
    val highPriorityNotes by sharedViewModel.highPriorityNotes.collectAsState()



    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState ,
        onComplete = { sharedViewModel.action.value = it},
        noteTitle = sharedViewModel.title.value,
        onUndoClicked = {
                        sharedViewModel.action.value = it
        },
        action = action
    )


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState

        ) },
        content = { ListContent(
            searchAppBarState = searchAppBarState,
            searchedNotes = searchedNotes,
            allNotes = allNotes,
            navigateToNoteScreen = navigateToNoteScreen,
            lowPriorityNotes = lowPriorityNotes,
            highPriorityNotes = highPriorityNotes,
            sortState = sortState,
            onSwipeToDelete = { action, note ->
                sharedViewModel.action.value = action
                sharedViewModel.updateNoteFields(selectedNote = note)
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            }
        )},
        floatingActionButton = {
            ListFab(onFabClicked = navigateToNoteScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (noteId: Int) -> Unit
) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.fabBackgroundColor,
        onClick = {
            onFabClicked(-1)

        }) {
        Icon(imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White)
    }
}

@Composable
fun DisplaySnackBar(
    onUndoClicked: (Action) -> Unit,
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    noteTitle: String,
    action: Action
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if(action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, noteTitle = noteTitle),
                    actionLabel = setActionLabel(action)
                )
                undoDeletedNote(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }

}

private fun setMessage(action: Action, noteTitle: String): String {
    return when(action) {
        Action.DELETE_ALL -> "All Tasks removed"
        else -> "${action.name}: $noteTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action == Action.DELETE ) {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedNote (
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if(snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE)
    {
        onUndoClicked(Action.UNDO)
    }
}

//@Composable
//@Preview
//private fun ListScreenPreview() {
//    ListScreen(navigateToNoteScreen = {})
//}