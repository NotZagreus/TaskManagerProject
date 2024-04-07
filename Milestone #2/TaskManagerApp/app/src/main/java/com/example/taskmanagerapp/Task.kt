package com.example.taskmanagerapp

import java.io.Serializable

data class Task(
    val creationTime: String,
    val dueTime: String,
    val creatorName: String,
    val taskDescription: String
) : Serializable
