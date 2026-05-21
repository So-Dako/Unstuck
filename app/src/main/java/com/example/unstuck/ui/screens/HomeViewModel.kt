package com.example.unstuck.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unstuck.TaskRepository
import com.example.unstuck.database.Task
import com.example.unstuck.database.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

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
}