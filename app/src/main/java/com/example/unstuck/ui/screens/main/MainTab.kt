package com.example.unstuck.ui.screens.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unstuck.R

enum class MainTab(val icon: ImageVector, @StringRes val label: Int) {
    HOME(Icons.Outlined.Home, R.string.tab_home),
    CALENDAR(Icons.Outlined.CalendarMonth, R.string.tab_calendar),
    STATS(Icons.Outlined.BarChart, R.string.tab_stats),
    PROFILE(Icons.Outlined.Settings, R.string.tab_settings)
}