package com.example.unstuck.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE category = :category ORDER BY date, time")
    fun getTaskByCategory(category: String): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date, time")
    fun getAllTaskFilteredByDate(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date = :date ORDER BY time")
    fun getTaskByDate(date: String): Flow<List<Task>>

    @Query("SELECT DISTINCT date FROM task")
    fun getAllDatesWithTasks(): Flow<List<String>>

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): Task
}