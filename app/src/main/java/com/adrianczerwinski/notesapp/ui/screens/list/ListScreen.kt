package com.adrianczerwinski.notesapp.ui.screens.list

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.adrianczerwinski.notesapp.R
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import com.adrianczerwinski.notesapp.ui.theme.fabBackgroundColor
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListScreen(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllNotes()
    }
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val allNotes by sharedViewModel.allNotes.collectAsState()
    val searchedNotes by sharedViewModel.searchedNotes.collectAsState()

    val action by sharedViewModel.action
    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState ,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action)},
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
            navigateToNoteScreen = navigateToNoteScreen
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
    handleDatabaseActions: () -> Unit,
    noteTitle: String,
    action: Action
) {
    handleDatabaseActions()
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