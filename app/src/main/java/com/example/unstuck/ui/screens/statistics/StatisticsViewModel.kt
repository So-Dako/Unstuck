package com.example.unstuck.ui.screens.statistics

import android.graphics.Point
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unstuck.TaskRepository
import com.example.unstuck.database.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    val selectedPeriod = MutableStateFlow(0)

    private val allTasks = repository.getAllTaskFilteredByDate()

    val statisticsState: StateFlow<StatisticsState> = combine(allTasks, selectedPeriod) { tasks, period ->
        calculateStatistics(tasks, period)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState()
    )

    fun changePeriod(index: Int) {
        selectedPeriod.value = index
    }

    private fun calculateStatistics(tasks: List<Task>, periodIndex: Int): StatisticsState {
        val now = LocalDate.now()

        val (currentStart, previousStart) = when (periodIndex) {
            0 -> Pair(now.minusWeeks(1), now.minusWeeks(2))
            1 -> Pair(now.minusMonths(1), now.minusMonths(2))
            else -> Pair(now.minusYears(1), now.minusYears(2))
        }

        val parsedTasks = tasks.mapNotNull { task ->
            val date = runCatching { LocalDate.parse(task.date) }.getOrNull()
            if (date != null) Pair(task, date) else null
        }

        val currentPeriodTasks = parsedTasks.filter { it.second.isAfter(currentStart) && !it.second.isAfter(now) }
        val currentCompleted = currentPeriodTasks.count { it.first.isDone }
        val currentUncompleted = currentPeriodTasks.count { !it.first.isDone }

        val previousPeriodTasks = parsedTasks.filter { it.second.isAfter(previousStart) && !it.second.isAfter(currentStart) }
        val previousCompleted = previousPeriodTasks.count { it.first.isDone }
        val previousUncompleted = previousPeriodTasks.count { !it.first.isDone }

        val completedTrend = calculatePercentageChange(currentCompleted, previousCompleted)
        val uncompletedTrend = calculatePercentageChange(currentUncompleted, previousUncompleted)

        return StatisticsState(
            completedCount = currentCompleted,
            uncompletedCount = currentUncompleted,
            completedTrend = completedTrend,
            uncompletedTrend = uncompletedTrend
        )
    }

    private fun calculatePercentageChange(current: Int, previous: Int): Int {
        if (previous == 0) return if (current > 0) 100 else 0
        return (((current - previous).toFloat() / previous) * 100).toInt()
    }
}