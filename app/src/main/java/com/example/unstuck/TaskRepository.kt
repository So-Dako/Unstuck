package com.example.unstuck

import com.example.unstuck.database.Task
import com.example.unstuck.database.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTaskByDate(date: LocalDate): Flow<List<Task>> {
        return taskDao.getTaskByDate(date.toString())
    }

    suspend fun upsertTask(newTask: Task): Int{
        return taskDao.upsertTask(newTask).toInt()
    }

    suspend fun updateTask(task: Task){
        taskDao.upsertTask(task)
    }

    fun getAllDatesWithTasks(): Flow<List<LocalDate>>{
        return taskDao.getAllDatesWithTasks()
            .map { stringList ->
                stringList.mapNotNull { dateString ->
                    runCatching { LocalDate.parse(dateString) }.getOrNull()
                }
            }
    }

    fun getAllTaskFilteredByDate():Flow<List<Task>>{
        return taskDao.getAllTaskFilteredByDate()
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(taskId: Int): Task{
        return taskDao.getTaskById(taskId = taskId)
    }
}