package com.example.unstuck.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(val icon: ImageVector, val label: String) {
    HOME(Icons.Outlined.Home, "Головна"),
    CALENDAR(Icons.Outlined.CalendarMonth, "Календар"),
    STATS(Icons.Outlined.BarChart, "Статистика"),
    PROFILE(Icons.Outlined.Person, "Профіль")
}