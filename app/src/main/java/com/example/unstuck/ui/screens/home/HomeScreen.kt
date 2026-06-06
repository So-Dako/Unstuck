package com.example.unstuck.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.unstuck.database.Task
import com.example.unstuck.ui.screens.elements.TaskCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onEditTaskClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.taskState.collectAsStateWithLifecycle()
    val greeting by viewModel.greetingState.collectAsStateWithLifecycle()

    val clickedTask by viewModel.clickedTaskState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.updateGreeting()
        }
    }

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")
    val formattedDate = LocalDate.now().format(dateFormatter)

    val completedTasksCount = tasks.count { it.isDone }
    val totalTasksCount = tasks.size
    val progress =
        if (totalTasksCount > 0) completedTasksCount.toFloat() / totalTasksCount else 0f

    clickedTask?.let { task ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = {
                Text(
                    text = "Оберіть дію",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Що ви хочете зробити із завданням \"${task.name}\"?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEditTaskClick(task.taskId)
                        viewModel.dismissDialog()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        "Редагувати",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTask(task)
                        viewModel.dismissDialog()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Видалити")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = greeting,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (tasks.isEmpty()) {
            item { Box(
                modifier = modifier
                    .fillParentMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("На сьогодні завдань немає")
            }}
        } else {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Денний прогрес",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(text = "$completedTasksCount з $totalTasksCount завдань виконано")
                    }
                    CircularProgressWithText(progress = progress)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Справи на сьогодні",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            formattedDate,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
                items(tasks, key = { it.taskId }) { task ->
                    TaskCard(
                        task = task,
                        onCheckedChange = { viewModel.toggleTaskStatus(task) },
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    viewModel.toggleTaskStatus(task)
                                },
                                onLongClick = {
                                    viewModel.onTaskClicked(task)
                                }
                            )
                    )
                }
            }
        }

@Composable
fun CircularProgressWithText(
    progress: Float,
    modifier: Modifier = Modifier
){
    val percentage = (progress * 100).toInt()

    Box(
        modifier = modifier.size(70.dp),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.size(70.dp),
            color = MaterialTheme.colorScheme.surface,
            strokeWidth = 8.dp,
            strokeCap = StrokeCap.Round
        )
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(70.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "$percentage%"
        )
    }
}
