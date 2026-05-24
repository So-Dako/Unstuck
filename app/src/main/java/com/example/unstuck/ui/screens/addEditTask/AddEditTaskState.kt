package com.example.unstuck.ui.screens.addEditTask

import java.time.LocalDate
import java.time.LocalTime

data class AddEditTaskState(
    val title: String,
    val date: LocalDate,
    val time: LocalTime,
    val notes: String,
    val remind: Boolean
)
