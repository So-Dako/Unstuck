package com.example.unstuck.ui.screens.addEditTask

import androidx.lifecycle.ViewModel
import com.example.unstuck.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(repository: TaskRepository) : ViewModel() {

}