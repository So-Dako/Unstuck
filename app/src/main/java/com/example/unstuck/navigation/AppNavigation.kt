package com.example.unstuck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.unstuck.ui.screens.addEditTask.AddEditTaskEvent
import com.example.unstuck.ui.screens.addEditTask.AddEditTaskViewModel
import com.example.unstuck.ui.screens.addEditTask.AddTask
import com.example.unstuck.ui.screens.home.HomeScreen
import com.example.unstuck.ui.screens.home.HomeViewModel

@Composable
fun AppNavigation(backStack: SnapshotStateList<Screens>){
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screens.Home> {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = homeViewModel,
                    onAdd = { backStack.add(Screens.AddTask) }
                )
            }
            entry<Screens.AddTask> {
                val addEditTaskViewModel: AddEditTaskViewModel = viewModel()
                AddTask(
                    viewModel = addEditTaskViewModel,
                    onBack = { backStack.removeLastOrNull() },
                    onSaveSuccess = { backStack.removeLastOrNull() }
                    )
            }
        }
    )
}