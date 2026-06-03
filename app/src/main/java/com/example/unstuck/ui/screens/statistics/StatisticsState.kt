package com.example.unstuck.ui.screens.statistics

import android.graphics.Point

data class StatisticsState(
    val completedCount: Int = 0,
    val uncompletedCount: Int = 0,
    val completedTrend: Int = 0,
    val uncompletedTrend: Int = 0
)