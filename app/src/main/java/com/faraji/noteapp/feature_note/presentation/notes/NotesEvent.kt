package com.faraji.noteapp.feature_note.presentation.notes

import com.faraji.noteapp.feature_note.domain.model.Note
import com.faraji.noteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class Delete(val note: Note) : NotesEvent()
    data class ClickedOnNote(val note: Note) : NotesEvent()
    object ClickedOnAddNote : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
