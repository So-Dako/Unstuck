package com.example.unstuck.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.navigation.Screens

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {


    val tasks by viewModel.taskState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Додаати"
                )
            }
        }
    ) { innerPadding ->
        if (tasks.isEmpty()){
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                Text("На сьогодні завдань немає")
            }
        }
        else {
            LazyColumn(
                modifier = modifier.padding(innerPadding),
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
                            task.time?.let{ Text(it) }
                        }
                    }
                }
            }
        }
    }
}