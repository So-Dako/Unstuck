package com.example.unstuck.navigation

import androidx.navigation3.runtime.NavKey
import com.example.unstuck.database.Task
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens: NavKey {
    @Serializable
    data object Main: Screens
    @Serializable
    data class AddEditTask(val taskId: Int? = null): Screens
}