package com.example.unstuck.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.unstuck.database.Task
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object TaskReminderManager {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleReminder(context: Context, task: Task) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) return
        }

        val localDate = LocalDate.parse(task.date)
        val localTime = LocalTime.parse(task.time)

        val zonedDateTime = localDate.atTime(localTime).atZone(ZoneId.systemDefault())
        val triggerTime = zonedDateTime.toInstant().toEpochMilli()

        if (triggerTime <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TASK_TITLE", task.name)
            putExtra("TASK_ID", task.taskId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context, taskId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}