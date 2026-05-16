package com.example.unstuck.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE category = :category ORDER BY startDate, startTime")
    fun getTaskByCategory(category: String): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY startDate, startTime")
    fun getTaskByDate(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE startDate = :date ORDER BY startTime")
    fun getTaskByDate(date: String): Flow<List<Task>>
}