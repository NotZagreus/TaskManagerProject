package com.example.taskmanagerapp

import TaskDbHelper
import android.content.Context

class DatabaseUtils {
    fun loadTasksFromDatabase(context: Context): MutableList<Task> {
        val dbHelper = TaskDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            TaskContract.TaskEntry.COLUMN_CREATION_TIME,
            TaskContract.TaskEntry.COLUMN_DUE_TIME,
            TaskContract.TaskEntry.COLUMN_CREATOR_NAME,
            TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION
        )

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val creationTime = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CREATION_TIME))
                val dueTime = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_TIME))
                val creatorName = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CREATOR_NAME))
                val taskDescription = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION))
                tasks.add(Task(creationTime, dueTime, creatorName, taskDescription))
            }
        }

        cursor.close()

        return tasks
    }
}