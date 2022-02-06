package com.adrianczerwinski.notesapp.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.repositories.NotesRepository
import com.adrianczerwinski.notesapp.data.util.Constants.MAX_TITLE_LENGTH
import com.adrianczerwinski.notesapp.data.util.RequestState
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.MEDIUM)


    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    var searchTextState: MutableState<String> = mutableStateOf("")

    private var _allNotes = MutableStateFlow<RequestState<List<NoteTask>>>(RequestState.Idle)
    val allNotes: StateFlow<RequestState<List<NoteTask>>> = _allNotes

    fun getAllNotes() {
        _allNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllNotes.collect {
                    _allNotes.value = RequestState.Success(it)
                }
            }

        } catch (e: Exception) {
            _allNotes.value = RequestState.Error(e)

        }

    }

    private val _selectedNote: MutableStateFlow<NoteTask?> = MutableStateFlow(null)
    val selectedNote: StateFlow<NoteTask?> = _selectedNote

    fun getSelectedNote(noteId: Int){
        viewModelScope.launch {
            repository.getSelectedNote(noteId).collect { note ->
                _selectedNote.value = note

            }
        }
    }

    fun updateNoteFields(selectedNote: NoteTask?){
        if(selectedNote != null) {
            id.value = selectedNote.id
            title.value = selectedNote.title
            description.value = selectedNote.description
            priority.value = selectedNote.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.MEDIUM

        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }


}