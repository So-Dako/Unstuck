package com.example.unstuck.ui.screens.addEditTask

import java.time.LocalDate
import java.time.LocalTime

data class AddEditTaskState(
    val title: String = "",
    val titleError: String? = null,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.of(0,0),
    val notes: String = "",
    val remind: Boolean = true,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val isEditMode: Boolean = false
)
