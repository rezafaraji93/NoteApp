package com.faraji.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraji.noteapp.feature_note.domain.model.InvalidNoteException
import com.faraji.noteapp.feature_note.domain.model.Note
import com.faraji.noteapp.feature_note.domain.use_case.NoteUseCases
import com.faraji.noteapp.feature_note.domain.util.NoteOrder
import com.faraji.noteapp.feature_note.domain.util.OrderType
import com.faraji.noteapp.feature_note.domain.util.UiEvent
import com.faraji.noteapp.feature_note.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class
                    && state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                } else {
                    getNotes(event.noteOrder)
                }
            }
            is NotesEvent.Delete -> {
                viewModelScope.launch {
                    useCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    try {
                        useCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                        recentlyDeletedNote = null
                    } catch (e: InvalidNoteException) {

                    }

                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.ClickedOnAddNote -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(Screen.AddEditNoteScreen.route)
                    )
                }
            }
            is NotesEvent.ClickedOnNote -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            Screen.AddEditNoteScreen.route +
                                    "?noteId=${event.note.id}&noteColor=${event.note.color}"
                        )
                    )
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotesUseCase(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

}