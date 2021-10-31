package com.faraji.noteapp.feature_note.domain.util

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class Navigate(val route: String): UiEvent()
    object SaveNote : UiEvent()
}