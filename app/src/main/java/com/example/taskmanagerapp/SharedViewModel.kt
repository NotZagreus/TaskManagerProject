package com.example.taskmanagerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// ViewModel class for sharing data between fragments
class SharedViewModel : ViewModel() {
    // Private mutable live data that holds a list of tasks
    private val _tasks = MutableLiveData<List<Task>>()
    // Public immutable live data that exposes the list of tasks
    val tasks: LiveData<List<Task>> get() = _tasks

    // Function to update the list of tasks
    fun updateTasks(newTasks: List<Task>) {
        _tasks.value = newTasks
    }
}