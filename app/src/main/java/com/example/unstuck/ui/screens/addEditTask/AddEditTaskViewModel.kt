package com.example.unstuck.ui.screens.addEditTask

import android.R.attr.category
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unstuck.TaskRepository
import com.example.unstuck.database.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _addEditTaskState = MutableStateFlow(AddEditTaskState())
    val addEditTaskState = _addEditTaskState.asStateFlow()

    fun onEvent(event: AddEditTaskEvent){
        when(event) {
            /*is AddEditTaskEvent.onDateChanged -> {
                _addEditTaskState.update { it.copy(date = event.date) }
            }

             */
            is AddEditTaskEvent.onNotesChanged -> {
                _addEditTaskState.update { it.copy(notes = event.notes) }
            }
            is AddEditTaskEvent.onRemindClicked -> {
                _addEditTaskState.update { it.copy(remind = event.remind) }
            }
            AddEditTaskEvent.onSaveTask -> {
                if (_addEditTaskState.value.titleError == null){
                    val newTask = Task(
                        name = _addEditTaskState.value.title,
                        date = _addEditTaskState.value.date.toString(),
                        time = _addEditTaskState.value.time.toString(),
                        notes = _addEditTaskState.value.notes,
                        remind = _addEditTaskState.value.remind,
                        isDone = false
                    )
                    viewModelScope.launch {
                        repository.addTask(newTask)
                    }
                }
            }
            is AddEditTaskEvent.onTimeChanged -> {
                _addEditTaskState.update {
                    it.copy(time = event.time)
                }
            }
            is AddEditTaskEvent.onTitleChanged -> {
                _addEditTaskState.update {
                    it.copy(
                        title = event.title,
                        titleError = null
                    )
                }
            }
            is AddEditTaskEvent.onDateSelected -> {
                _addEditTaskState.update {
                    it.copy(
                        date = event.selectedDate,
                        showDatePicker = false
                    )
                }
            }
            AddEditTaskEvent.onDateTextFieldClicked -> {
                _addEditTaskState.update {
                    it.copy(showDatePicker = true)
                }
            }
            AddEditTaskEvent.onDismissDatePicker -> {
                _addEditTaskState.update {
                    it.copy(showDatePicker = false)
                }
            }
        }
    }
}