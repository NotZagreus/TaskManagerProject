package com.example.taskmanagerapp

import TaskDbHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListFragment : Fragment() {

    // RecyclerView for displaying tasks
    private lateinit var taskRecyclerView: RecyclerView
    // List of tasks to be displayed
    private lateinit var taskList: MutableList<Task>
    // Adapter for the RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    // ViewModel for sharing data between fragments
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })

    // Called to have the fragment instantiate its user interface view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        // Initialize RecyclerView, task list, and adapter
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        taskList = mutableListOf()
        taskAdapter = TaskAdapter(requireContext(), taskList, this)
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    // Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in the shared ViewModel
        sharedViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            // Update task list and notify adapter of changes
            taskList.clear()
            taskList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
        })

        // Load tasks from database
        loadTasksFromDatabase()
    }

    // Deletes a task from the database and updates the RecyclerView
    fun deleteTask(task: Task) {
        val dbHelper = TaskDbHelper(requireContext())
        val db = dbHelper.writableDatabase

        // Define 'where' part of query.
        val selection = "${TaskContract.TaskEntry.COLUMN_CREATION_TIME} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(task.creationTime)
        // Issue SQL statement.
        val deletedRows = db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs)

        if (deletedRows > 0) {
            // Task was deleted successfully
            // Remove the task from the list and update the RecyclerView
            taskList.remove(task)
            taskAdapter.notifyDataSetChanged()
        }
    }

    // Loads tasks from the database and updates the shared ViewModel
    private fun loadTasksFromDatabase() {
        val dbHelper = TaskDbHelper(requireContext())
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

        // Update the shared ViewModel with the loaded tasks
        sharedViewModel.updateTasks(tasks)
    }
}