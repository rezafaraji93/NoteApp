package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.faraji.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.faraji.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.faraji.noteapp.feature_note.domain.use_case.GetNotesUseCase

data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase
)
