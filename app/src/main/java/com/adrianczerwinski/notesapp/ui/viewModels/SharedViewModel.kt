package com.adrianczerwinski.notesapp.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrianczerwinski.notesapp.data.models.NoteTask
import com.adrianczerwinski.notesapp.data.models.Priority
import com.adrianczerwinski.notesapp.data.repositories.DataStoreRepository
import com.adrianczerwinski.notesapp.data.repositories.NotesRepository
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.Constants.MAX_TITLE_LENGTH
import com.adrianczerwinski.notesapp.data.util.RequestState
import com.adrianczerwinski.notesapp.data.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private var _allNotes = MutableStateFlow<RequestState<List<NoteTask>>>(RequestState.Idle)
    val allNotes: StateFlow<RequestState<List<NoteTask>>> = _allNotes

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    init {
        getAllNotes()
        readSortState()
    }

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.MEDIUM)


    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    var searchTextState: MutableState<String> = mutableStateOf("")


    private var _searchedNotes = MutableStateFlow<RequestState<List<NoteTask>>>(RequestState.Idle)
    val searchedNotes: StateFlow<RequestState<List<NoteTask>>> = _searchedNotes

    private fun getAllNotes() {
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

    fun searchDataBase(searchQuery: String) {
        _searchedNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedNotes ->
                        _searchedNotes.value = RequestState.Success(searchedNotes)
                    }
            }

        } catch (e: Exception) {
            _searchedNotes.value = RequestState.Error(e)

        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED

    }

    val lowPriorityNotes: StateFlow<List<NoteTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityNotes: StateFlow<List<NoteTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )


    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }

        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)

        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    private val _selectedNote: MutableStateFlow<NoteTask?> = MutableStateFlow(null)
    val selectedNote: StateFlow<NoteTask?> = _selectedNote

    fun getSelectedNote(noteId: Int) {
        viewModelScope.launch {
            repository.getSelectedNote(noteId).collect { note ->
                _selectedNote.value = note

            }
        }
    }

    private fun addNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val noteTask = NoteTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addNote(noteTask = noteTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }


    private fun updateNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val noteTask = NoteTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateNote(noteTask = noteTask)
        }
    }

    private fun deleteNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val noteTask = NoteTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteNote(noteTask = noteTask)
        }
    }

    private fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addNote()
            }
            Action.UPDATE -> {
                updateNote()
            }
            Action.DELETE -> {
                deleteNote()
            }
            Action.DELETE_ALL -> {
                deleteAllNotes()
            }
            Action.UNDO -> {
                addNote()
            }
            else -> {
            }
        }
    }

    fun updateNoteFields(selectedNote: NoteTask?) {
        if (selectedNote != null) {
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
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }


}