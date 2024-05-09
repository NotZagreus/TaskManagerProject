package com.example.taskmanagerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val context: Context, private val taskList: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creationTime: TextView = view.findViewById(R.id.creationTime)
        val dueTime: TextView = view.findViewById(R.id.dueTime)
        val creatorName: TextView = view.findViewById(R.id.creatorName)
        val taskDescription: TextView = view.findViewById(R.id.taskDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.creationTime.text = task.creationTime
        holder.dueTime.text = task.dueTime
        holder.creatorName.text = task.creatorName
        holder.taskDescription.text = task.taskDescription
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}