package com.example.taskmanagerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for the RecyclerView in TaskListFragment
class TaskAdapter(
    private val context: Context, // The context where the RecyclerView is running
    private val taskList: MutableList<Task>, // The data set of the adapter
    private val fragment: TaskListFragment // Reference to the fragment that interacts with this adapter
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder provides a reference to the views for each data item
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creationTime: TextView = view.findViewById(R.id.creationTime)
        val dueTime: TextView = view.findViewById(R.id.dueTime)
        val creatorName: TextView = view.findViewById(R.id.creatorName)
        val taskDescription: TextView = view.findViewById(R.id.taskDescription)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.creationTime.text = task.creationTime
        holder.dueTime.text = task.dueTime
        holder.creatorName.text = task.creatorName
        holder.taskDescription.text = task.taskDescription

        // Set the click listener for the delete button
        holder.deleteButton.setOnClickListener {
            // Delete task from your data source
            fragment.deleteTask(task)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return taskList.size
    }
}