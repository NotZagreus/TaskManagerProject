package com.example.taskmanagerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class TaskListFragment : Fragment() {

    private lateinit var taskListView: ListView
    private lateinit var taskList: MutableList<Task>
    private lateinit var taskAdapter: ArrayAdapter<Task>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskListView = view.findViewById(R.id.taskListView)
        taskList = mutableListOf()
        taskAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, taskList)
        taskListView.adapter = taskAdapter

        loadTasksFromFile()

        return view
    }

    private fun loadTasksFromFile() {
        val tasks = mutableListOf<Task>()
        val filename = "tasks.txt"

        try {
            val fileInputStream = requireContext().openFileInput(filename)
            val inputStreamReader = fileInputStream.bufferedReader()
            val lines = inputStreamReader.readLines()
            inputStreamReader.close()

            for (line in lines) {
                val parts = line.split(", ")
                if (parts.size == 4) {
                    val task = Task(parts[0], parts[1], parts[2], parts[3])
                    tasks.add(task)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        taskList.clear()
        taskList.addAll(tasks)
        taskAdapter.notifyDataSetChanged()
    }

    fun addTaskToList(task: Task) {
        taskList.add(task)
        taskAdapter.notifyDataSetChanged()
    }
}