package com.example.unstuck.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val name: String,
    val startDate: String,
    val startTime: String?,
    val endDate: String?,
    val endTime: String?,
    val category: String?,
    val notes: String?,
    val remind: Boolean,
    val isDone: Boolean
)
