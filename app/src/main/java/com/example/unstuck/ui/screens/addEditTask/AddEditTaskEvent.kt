package com.example.unstuck.ui.screens.addEditTask

import android.content.Context
import java.time.LocalDate
import java.time.LocalTime

sealed interface AddEditTaskEvent {
    data class OnTitleChanged(val title: String): AddEditTaskEvent
    data class OnNotesChanged(val notes: String): AddEditTaskEvent
    data class OnRemindClicked(val remind: Boolean): AddEditTaskEvent
    data class OnSaveTask(val context: Context): AddEditTaskEvent
    object OnDateTextFieldClicked: AddEditTaskEvent
    object OnDismissDatePicker: AddEditTaskEvent
    data class OnDateSelected(val selectedDate: LocalDate): AddEditTaskEvent
    object OnTimeTextFieldClicked: AddEditTaskEvent
    object OnDismissTimePicker: AddEditTaskEvent
    data class OnTimeSelected(val selectedTime: LocalTime): AddEditTaskEvent
}