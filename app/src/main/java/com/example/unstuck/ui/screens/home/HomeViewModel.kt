package com.example.unstuck.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unstuck.R
import com.example.unstuck.TaskRepository
import com.example.unstuck.database.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    val taskState: StateFlow<List<Task>> = repository
        .getTaskByDate(LocalDate.now())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _greetingState = MutableStateFlow(getGreetingMessage())
    val greetingState: StateFlow<Int> = _greetingState.asStateFlow()


    private val _clickedTaskState = MutableStateFlow<Task?>(null)
    val clickedTaskState: StateFlow<Task?> = _clickedTaskState.asStateFlow()

    fun updateGreeting() {
        _greetingState.value = getGreetingMessage()
    }

    private fun getGreetingMessage(): Int {
        return when (LocalTime.now().hour) {
            in 6..11 -> R.string.greeting_morning
            in 12..16 -> R.string.greeting_day
            in 17..22 -> R.string.greeting_evening
            else -> R.string.greeting_night
        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = !task.isDone))
        }
    }

    //task dialog

    fun onTaskClicked(task: Task) {
        _clickedTaskState.value = task
    }

    fun dismissDialog() {
        _clickedTaskState.value = null
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            viewModelScope.launch {
                repository.deleteTask(task)
            }
            dismissDialog()
        }
    }
}