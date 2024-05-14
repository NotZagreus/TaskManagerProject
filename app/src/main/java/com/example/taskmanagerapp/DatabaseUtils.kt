package com.example.taskmanagerapp

import TaskDbHelper
import android.content.Context

// Utility class for database operations
class DatabaseUtils {
    // Function to load tasks from the database
    fun loadTasksFromDatabase(context: Context): MutableList<Task> {
        // Create a helper object to manage the database
        val dbHelper = TaskDbHelper(context)
        // Get a readable database
        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database you will use
        val projection = arrayOf(
            TaskContract.TaskEntry.COLUMN_CREATION_TIME,
            TaskContract.TaskEntry.COLUMN_DUE_TIME,
            TaskContract.TaskEntry.COLUMN_CREATOR_NAME,
            TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION
        )

        // Perform a query on the tasks table
        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        // List to hold Task objects
        val tasks = mutableListOf<Task>()
        with(cursor) {
            // Iterate over the returned cursor
            while (moveToNext()) {
                // Get values from the cursor
                val creationTime = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CREATION_TIME))
                val dueTime = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_TIME))
                val creatorName = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CREATOR_NAME))
                val taskDescription = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION))
                // Create a Task object and add it to the list
                tasks.add(Task(creationTime, dueTime, creatorName, taskDescription))
            }
        }

        // Close the cursor to release its resources
        cursor.close()

        // Return the list of tasks
        return tasks
    }
}