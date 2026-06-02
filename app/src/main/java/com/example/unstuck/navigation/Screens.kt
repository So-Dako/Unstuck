package com.example.unstuck.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screens: NavKey {
    @Serializable
    data object Main: Screens
    @Serializable
    data object AddTask: Screens
}