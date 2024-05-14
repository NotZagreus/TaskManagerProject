package com.example.taskmanagerapp

import java.io.Serializable

// Data class representing a Task
data class Task(
    val creationTime: String,
    val dueTime: String,
    val creatorName: String,
    val taskDescription: String
) : Serializable // Implementing Serializable to allow Task objects to be passed between activities