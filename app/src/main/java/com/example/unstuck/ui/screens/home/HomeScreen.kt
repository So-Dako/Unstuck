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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.database.Task
import com.example.unstuck.ui.theme.UnstuckTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

@Composable
fun HomeScreenNew(
    modifier: Modifier = Modifier
) {
    val tasks = remember {mutableStateListOf(
        Task(
            taskId = 0,
            name = "Забрати посилку з Нової Пошти",
            date = "2026-06-02",
            time = "18:30",
            notes = "Номер накладної: 20450012345678. Зберігання до суботи!",
            remind = true,
            isDone = false
        ),
        Task(
            taskId = 1,
            name = "Урок англійської мови",
            date = "2026-06-03",
            time = "19:00",
            notes = "Повторити неправильні дієслова та дочитати статтю про Coroutines.",
            remind = true,
            isDone = false
        ),
    )}

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")
    val formattedDate = LocalDate.now().format(dateFormatter)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
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
                            Text("х з х завдань виконано")
                        }
                        CircularProgressWithText(0.7f)
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
                itemsIndexed(tasks) { index, task ->
                //items(tasks) { task ->
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
                        IconButton(onClick = { tasks[index] = task.copy(isDone = !task.isDone) }) {
                            Icon(
                                imageVector = if (task.isDone) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Outlined.RadioButtonUnchecked
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
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

@Preview(showSystemUi = true)
@Composable
fun HomeNewPreview(){
    UnstuckTheme {
        Scaffold()
        { innerPadding ->
            HomeScreenNew(modifier = Modifier.padding(innerPadding))
        }
    }
}