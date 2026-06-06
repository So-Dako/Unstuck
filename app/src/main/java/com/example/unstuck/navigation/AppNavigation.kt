package com.example.unstuck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.unstuck.ui.screens.addEditTask.AddEditTaskViewModel
import com.example.unstuck.ui.screens.addEditTask.AddEditTask
import com.example.unstuck.ui.screens.main.MainScreen

@Composable
fun AppNavigation(backStack: SnapshotStateList<Screens>){
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screens.Main> {
                MainScreen(
                    onNavigateToAddTask = { backStack.add(Screens.AddEditTask()) },
                    onEditTaskClick = { taskId ->
                        backStack.add(Screens.AddEditTask(taskId = taskId))
                    }
                )
            }
            entry<Screens.AddEditTask> { backStackEntry ->
                val taskId = backStackEntry.taskId

                val addEditTaskViewModel: AddEditTaskViewModel = hiltViewModel()

                LaunchedEffect(taskId) {
                    addEditTaskViewModel.initialize(taskId)
                }

                AddEditTask(
                    viewModel = addEditTaskViewModel,
                    isEdit = taskId != null,
                    onBack = { backStack.removeLastOrNull() },
                    onSaveSuccess = { backStack.removeLastOrNull() }
                    )
            }
        }
    )
}