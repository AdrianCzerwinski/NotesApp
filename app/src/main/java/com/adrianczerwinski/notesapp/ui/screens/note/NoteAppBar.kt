package com.adrianczerwinski.notesapp.ui.screens.note

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.adrianczerwinski.notesapp.R
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.ui.theme.topAppBarBackgroundColor
import com.adrianczerwinski.notesapp.ui.theme.topAppBarContentColor

@Composable
fun NoteAppBar(
    selectedNote: NoteTask?,
    navigateToListScreen:(Action) -> Unit
){

    if(selectedNote == null) {
        NewNoteAppBar(navigateToListScreen = navigateToListScreen)
    } else
    {
        ExistingNoteAppBar(
            selectedNote = selectedNote,
            navigateToListScreen = navigateToListScreen
        )
    }


}



@Composable
fun NewNoteAppBar(
    navigateToListScreen:(Action) -> Unit
){
    TopAppBar(
        navigationIcon = {
                    BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_note),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun ExistingNoteAppBar(
    selectedNote: NoteTask,
    navigateToListScreen:(Action) -> Unit
){
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedNote.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            DeleteAction(onDeleteClicked = navigateToListScreen)
            UpdateAction(onUpdateClicked = navigateToListScreen)
        }
    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = {onCloseClicked(Action.NO_ACTION)}) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close_icone ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit
) {
    IconButton(onClick = {onDeleteClicked(Action.DELETE)}) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_all ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = {onUpdateClicked(Action.UPDATE)}) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.update_icon ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = {onBackClicked(Action.NO_ACTION)}) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back_arrow ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = {onAddClicked(Action.ADD)}) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.add_note),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

}

@Composable
@Preview
fun AddAppBarPreview(){
    NewNoteAppBar(navigateToListScreen = {})
}

@Composable
@Preview
fun UpdateAppBarPreview(){
    ExistingNoteAppBar(
        selectedNote = NoteTask(0,
            title = "Adrian Czerwinski",
            description = "Some random text",
            priority = Priority.MEDIUM
            ),
        navigateToListScreen = {})
}