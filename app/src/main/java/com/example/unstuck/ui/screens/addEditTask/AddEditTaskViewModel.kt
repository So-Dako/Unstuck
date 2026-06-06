package com.example.unstuck.ui.screens.addEditTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unstuck.TaskRepository
import com.example.unstuck.database.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _addEditTaskState = MutableStateFlow(AddEditTaskState())
    val addEditTaskState = _addEditTaskState.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    private var currentTaskId: Int? = null

    fun initialize(taskId: Int?) {
        _isSaved.value = false
        currentTaskId = taskId
        if (taskId != null) {
            viewModelScope.launch {
                try {
                    val task = repository.getTaskById(taskId)
                    task.let { existingTask ->
                        _addEditTaskState.update {
                            it.copy(
                                title = existingTask.name,
                                date = LocalDate.parse(existingTask.date),
                                time = LocalTime.parse(existingTask.time),
                                notes = existingTask.notes ?: "",
                                remind = existingTask.remind,
                                isEditMode = taskId != null
                            )
                        }
                    }
                } catch (e: Exception) {
                }
            }
        } else {
            _addEditTaskState.value = AddEditTaskState()
        }
    }

    fun resetSaveStatus() {
        _isSaved.value = false
    }

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.OnNotesChanged -> {
                _addEditTaskState.update { it.copy(notes = event.notes) }
            }

            is AddEditTaskEvent.OnRemindClicked -> {
                _addEditTaskState.update { it.copy(remind = event.remind) }
            }

            AddEditTaskEvent.OnSaveTask -> {
                val currentTitle = _addEditTaskState.value.title.trim()

                if (currentTitle.isEmpty()) {
                    _addEditTaskState.value = _addEditTaskState.value.copy(
                        titleError = "Назва не може бути порожньою!"
                    )
                } else {
                    val newTask = Task(
                        taskId = currentTaskId ?: 0,
                        name = _addEditTaskState.value.title,
                        date = _addEditTaskState.value.date.toString(),
                        time = _addEditTaskState.value.time.toString(),
                        notes = _addEditTaskState.value.notes.takeIf { it.isNotBlank() },
                        remind = _addEditTaskState.value.remind,
                        isDone = false
                    )
                    viewModelScope.launch {
                        try {
                            repository.addTask(newTask)
                            _addEditTaskState.value = AddEditTaskState()
                            _isSaved.value = true
                        } catch (e: Exception) {
                        }
                    }
                }
            }

            is AddEditTaskEvent.OnTitleChanged -> {
                _addEditTaskState.update {
                    it.copy(
                        title = event.title,
                        titleError = null
                    )
                }
            }
            is AddEditTaskEvent.OnDateSelected -> {
                _addEditTaskState.update {
                    it.copy(
                        date = event.selectedDate,
                        showDatePicker = false
                    )
                }
            }
            AddEditTaskEvent.OnDateTextFieldClicked -> {
                _addEditTaskState.update {
                    it.copy(showDatePicker = true)
                }
            }
            AddEditTaskEvent.OnDismissDatePicker -> {
                _addEditTaskState.update {
                    it.copy(showDatePicker = false)
                }
            }

            AddEditTaskEvent.OnDismissTimePicker -> {
                _addEditTaskState.update {
                    it.copy(showTimePicker = false)
                }
            }
            is AddEditTaskEvent.OnTimeSelected -> {
                _addEditTaskState.update {
                    it.copy(
                        time = event.selectedTime,
                        showTimePicker = false
                    )
                }
            }
            AddEditTaskEvent.OnTimeTextFieldClicked -> {
                _addEditTaskState.update {
                    it.copy(showTimePicker = true)
                }
            }
        }
    }
}