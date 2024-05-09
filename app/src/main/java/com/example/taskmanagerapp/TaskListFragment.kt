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

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskList: MutableList<Task>
    private lateinit var taskAdapter: TaskAdapter
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        taskList = mutableListOf()
        taskAdapter = TaskAdapter(requireContext(), taskList)
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            taskList.clear()
            taskList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
        })

        loadTasksFromDatabase()
    }

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

        sharedViewModel.updateTasks(tasks)
    }
}