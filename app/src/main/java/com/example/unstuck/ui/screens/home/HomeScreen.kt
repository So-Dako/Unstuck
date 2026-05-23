package com.example.unstuck.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    val tasks by homeViewModel.taskState.collectAsStateWithLifecycle()

    Surface {
        if (tasks.isEmpty()){
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text("На сьогодні завдань немає")
            }
        }
        else {
            LazyColumn(
                modifier = modifier
            ) {
                items(tasks) { task ->
                    Row {
                        IconButton(onClick = { !task.isDone }) {
                            Icon(
                                imageVector = if (task.isDone) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Outlined.RadioButtonUnchecked
                                },
                                contentDescription = ""
                            )
                        }
                        Column() {
                            Text(task.name)
                            task.endTime?.let{ Text(it) }
                        }
                    }
                }
            }
        }
    }
}