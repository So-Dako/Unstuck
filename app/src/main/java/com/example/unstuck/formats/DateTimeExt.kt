package com.example.unstuck.formats

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toFormattedString(): String {
    val timeString = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(timeString)
}

fun LocalDate.toFormattedString(): String {
    val dateString = DateTimeFormatter.ofPattern("d MMMM")
    return this.format(dateString)
}