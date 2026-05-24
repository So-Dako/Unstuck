package com.example.unstuck.ui.screens.addEditTask

import java.time.LocalDate
import java.time.LocalTime

sealed interface AddEditTaskEvent {
    data class onTitleChanged(val tittle: String): AddEditTaskEvent
    data class onDateChanged(val date: LocalDate): AddEditTaskEvent
    data class onTimeChanged(val time: LocalTime): AddEditTaskEvent
    data class onNotesChanged(val notes: String): AddEditTaskEvent
    data class onRemindClicked(val remind: Boolean): AddEditTaskEvent
    object onSaveTask: AddEditTaskEvent
}