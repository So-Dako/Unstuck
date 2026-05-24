package com.example.unstuck.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val name: String,
    val date: String,
    val time: String?,
    val category: String? = null,
    val notes: String?,
    val remind: Boolean,
    val isDone: Boolean
)
