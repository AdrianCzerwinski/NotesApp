package com.adrianczerwinski.notesapp.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.RequestState
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import com.adrianczerwinski.notesapp.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun ListContent(
    allNotes: RequestState<List<NoteTask>>,
    searchedNotes: RequestState<List<NoteTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToNoteScreen: (noteId: Int)  -> Unit,
    lowPriorityNotes: List<NoteTask>,
    highPriorityNotes: List<NoteTask>,
    sortState: RequestState<Priority>
) {

    if (sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if(searchedNotes is RequestState.Success) {
                    HandleListContent(
                        notes = searchedNotes.data,
                        navigateToNoteScreen = navigateToNoteScreen
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allNotes is RequestState.Success){
                    HandleListContent(notes = allNotes.data, navigateToNoteScreen = navigateToNoteScreen)
                }
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    notes = highPriorityNotes,
                    navigateToNoteScreen = navigateToNoteScreen
                )
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    notes = lowPriorityNotes,
                    navigateToNoteScreen = navigateToNoteScreen
                )
            }
        }


        }

    }






@ExperimentalMaterialApi
@Composable

fun HandleListContent(
    notes: List<NoteTask>,
    navigateToNoteScreen: (noteId: Int) -> Unit
){
    if(notes.isEmpty()) {
        EmptyContent()
    } else {
        DisplayNotes(notes = notes, navigateToNoteScreen = navigateToNoteScreen)
    }

}

@ExperimentalMaterialApi
@Composable
fun DisplayNotes(
    notes: List<NoteTask>,
    navigateToNoteScreen: (noteId: Int)  -> Unit
) {
    LazyColumn {
        items(
            items = notes,
            key = { note ->
                note.id
            }
        ) { note ->
            NoteItem(note = note, navigateToNoteScreen = navigateToNoteScreen)

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun NoteItem(
    note: NoteTask,
    navigateToNoteScreen: (noteId: Int)  -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.noteItemBackgroundColor,
        shape = RectangleShape,
        elevation = NOT_ITEM_ELEVATION,
        onClick = {
            navigateToNoteScreen(note.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()

        ) {
            Row {
                Text(
                    text = note.title,
                    color = MaterialTheme.colors.noteItemTextColor,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(8f)
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .width(PRIORITY_INDICATOR_SIZE)
                            .height(PRIORITY_INDICATOR_SIZE)
                            ) {
                                 drawCircle(
                                     color = note.priority.color
                                 )
                            }


                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = note.description,
                color = MaterialTheme.colors.noteItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }

    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun NotePreview (){
    NoteItem(note = NoteTask(
        id=0,
        title = "Title",
        description = "Some random text",
        priority = Priority.LOW
    ),
        navigateToNoteScreen = {}
    )
}