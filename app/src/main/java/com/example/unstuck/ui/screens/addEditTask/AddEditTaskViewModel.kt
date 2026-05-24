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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    var addEditTaskState by mutableStateOf(AddEditTaskState())
    private set

    fun onEvent(event: AddEditTaskEvent){
        when(event) {
            is AddEditTaskEvent.onDateChanged -> {
                addEditTaskState.copy(date = event.date)
            }
            is AddEditTaskEvent.onNotesChanged -> {
                addEditTaskState.copy(notes = event.notes)
            }
            is AddEditTaskEvent.onRemindClicked -> {
                addEditTaskState.copy(remind = !event.remind)
            }
            AddEditTaskEvent.onSaveTask -> {
                if (addEditTaskState.titleError == null){
                    val newTask = Task(
                        name = addEditTaskState.title,
                        date = addEditTaskState.date.toString(),
                        time = addEditTaskState.time.toString(),
                        notes = addEditTaskState.notes,
                        remind = addEditTaskState.remind,
                        isDone = false
                    )
                    viewModelScope.launch {
                        repository.addTask(newTask)
                    }
                }
            }
            is AddEditTaskEvent.onTimeChanged -> {
                addEditTaskState.copy(time = event.time)
            }
            is AddEditTaskEvent.onTitleChanged -> {
                addEditTaskState.copy(
                    title = event.title,
                    titleError = null
                )
            }
        }
    }
}