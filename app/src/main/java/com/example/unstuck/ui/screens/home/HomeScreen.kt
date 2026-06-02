package com.example.unstuck.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.database.Task
import com.example.unstuck.ui.theme.UnstuckTheme
import com.google.common.collect.Multimaps.index
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.taskState.collectAsStateWithLifecycle()

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")
    val formattedDate = LocalDate.now().format(dateFormatter)

    val completedTasksCount = tasks.count { it.isDone }
    val totalTasksCount = tasks.size
    val progressFraction = if (totalTasksCount > 0) completedTasksCount.toFloat() / totalTasksCount else 0f

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAdd() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Додати"
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item{
                    Text(
                        "Доброго ранку",
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            modifier = Modifier.weight(1f)
                        ){
                            Text(
                                "Денний прогрес",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                                )
                            Text(text = "$completedTasksCount з $totalTasksCount завдань виконано")
                        }
                        CircularProgressWithText(progress = progressFraction)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            "Справи на сьогодні",
                            style = MaterialTheme.typography.titleLarge)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                )
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ){
                            Text(
                                formattedDate,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(tasks, key = { it.taskId }) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.toggleTaskStatus(task) }) {
                            Icon(
                                imageVector = if (task.isDone) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Outlined.RadioButtonUnchecked
                                },
                                contentDescription = null,
                                tint = if (task.isDone) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                }
                            )
                        }
                        Column() {
                            Text(
                                task.name,
                                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (task.isDone) {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            task.time?.let{
                                Text(
                                    it,
                                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (task.isDone) {
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }
            }
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
