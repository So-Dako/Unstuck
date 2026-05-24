package com.example.unstuck.ui.screens.addEditTask

import java.time.LocalDate
import java.time.LocalTime

data class AddEditTaskState(
    val title: String = "",
    val titleError: String? = "Назва не може бути порожньою!",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val notes: String = "",
    val remind: Boolean = true
)
