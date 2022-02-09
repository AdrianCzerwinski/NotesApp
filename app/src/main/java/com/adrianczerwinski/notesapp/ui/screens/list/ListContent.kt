package com.adrianczerwinski.notesapp.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.adrianczerwinski.notesapp.R
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.RequestState
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import com.adrianczerwinski.notesapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListContent(
    allNotes: RequestState<List<NoteTask>>,
    searchedNotes: RequestState<List<NoteTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToNoteScreen: (noteId: Int)  -> Unit,
    lowPriorityNotes: List<NoteTask>,
    highPriorityNotes: List<NoteTask>,
    sortState: RequestState<Priority>,
    onSwipeToDelete: (Action, NoteTask) -> Unit
) {

    if (sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if(searchedNotes is RequestState.Success) {
                    HandleListContent(
                        notes = searchedNotes.data,
                        navigateToNoteScreen = navigateToNoteScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allNotes is RequestState.Success){
                    HandleListContent(
                        notes = allNotes.data,
                        navigateToNoteScreen = navigateToNoteScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    notes = highPriorityNotes,
                    navigateToNoteScreen = navigateToNoteScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    notes = lowPriorityNotes,
                    navigateToNoteScreen = navigateToNoteScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
        }


        }

    }

@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = LARGE_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_note),
            tint = Color.White
        )
    }
}




@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable

fun HandleListContent(
    notes: List<NoteTask>,
    onSwipeToDelete: (Action, NoteTask) -> Unit,
    navigateToNoteScreen: (noteId: Int) -> Unit
){
    if(notes.isEmpty()) {
        EmptyContent()
    } else {
        DisplayNotes(
            notes = notes,
            navigateToNoteScreen = navigateToNoteScreen,
            onSwipeToDelete = onSwipeToDelete
        )
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayNotes(
    notes: List<NoteTask>,
    onSwipeToDelete: (Action, NoteTask) -> Unit,
    navigateToNoteScreen: (noteId: Int)  -> Unit
) {
    LazyColumn {
        items(
            items = notes,
            key = { note ->
                note.id
            }
        ) { note ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if(isDismissed && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE, note)
                }

            }

            val degrees by animateFloatAsState(
                targetValue = if(dismissState.targetValue == DismissValue.Default) 0f else -45f
            )
            var itemAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true){
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))

                ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(fraction = 0.3f)},
                    background = { RedBackground(degrees = degrees ) },
                    dismissContent = {
                        NoteItem(note = note, navigateToNoteScreen = navigateToNoteScreen)
                    }
                )
            }
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