package com.example.taskmanagerapp

import android.provider.BaseColumns

// Contract class for the tasks database, defining the structure of the tasks table
object TaskContract {
    // Inner object that defines the contents of the tasks table
    object TaskEntry : BaseColumns {
        // Name of the tasks table
        const val TABLE_NAME = "tasks"
        const val _ID = BaseColumns._ID
        const val COLUMN_CREATION_TIME = "creation_time"
        const val COLUMN_DUE_TIME = "due_time"
        const val COLUMN_CREATOR_NAME = "creator_name"
        const val COLUMN_TASK_DESCRIPTION = "task_description"
    }
}