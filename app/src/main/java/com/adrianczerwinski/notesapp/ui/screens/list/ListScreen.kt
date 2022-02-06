package com.adrianczerwinski.notesapp.ui.screens.list

import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adrianczerwinski.notesapp.R
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import com.adrianczerwinski.notesapp.ui.theme.fabBackgroundColor
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel

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


    Scaffold(
        topBar = {
            ListAppBar(sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState

        ) },
        content = { ListContent(
            notes = allNotes,
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

//@Composable
//@Preview
//private fun ListScreenPreview() {
//    ListScreen(navigateToNoteScreen = {})
//}