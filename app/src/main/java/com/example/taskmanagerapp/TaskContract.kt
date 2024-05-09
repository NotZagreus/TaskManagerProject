package com.example.taskmanagerapp

import android.provider.BaseColumns

object TaskContract {
    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val _ID = BaseColumns._ID
        const val COLUMN_CREATION_TIME = "creation_time"
        const val COLUMN_DUE_TIME = "due_time"
        const val COLUMN_CREATOR_NAME = "creator_name"
        const val COLUMN_TASK_DESCRIPTION = "task_description"
    }
}