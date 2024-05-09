package com.example.taskmanagerapp

import TaskDbHelper
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })

    private lateinit var editTextDueTime: EditText
    private lateinit var editTextCreatorName: EditText
    private lateinit var editTextTaskDescription: EditText
    private lateinit var buttonAddTask: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDueTime = view.findViewById(R.id.editTextDueTime)
        editTextCreatorName = view.findViewById(R.id.editTextCreatorName)
        editTextTaskDescription = view.findViewById(R.id.editTextTaskDescription)
        buttonAddTask = view.findViewById(R.id.buttonAddTask)

        editTextDueTime.setOnClickListener {
            showDateTimePicker()
        }

        buttonAddTask.setOnClickListener {
            addTask()
        }
    }


    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)

                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val formattedDateTime = sdf.format(selectedDateTime.time)

                        editTextDueTime.setText(formattedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }


    private fun addTask() {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        val dueTime = editTextDueTime.text.toString().trim()
        val creatorName = editTextCreatorName.text.toString().trim()
        val taskDescription = editTextTaskDescription.text.toString().trim()

        if (dueTime.isEmpty() || creatorName.isEmpty() || taskDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = TaskDbHelper(requireContext())
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_CREATION_TIME, currentDateTime)
            put(TaskContract.TaskEntry.COLUMN_DUE_TIME, dueTime)
            put(TaskContract.TaskEntry.COLUMN_CREATOR_NAME, creatorName)
            put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION, taskDescription)
        }

        val newRowId = db?.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)

        // Close the database connection
        dbHelper.close()

        // Clear input fields after adding task
        editTextDueTime.text.clear()
        editTextCreatorName.text.clear()
        editTextTaskDescription.text.clear()

        if (newRowId != null && newRowId > -1) {
            val databaseUtils = DatabaseUtils()
            val tasks = databaseUtils.loadTasksFromDatabase(requireContext())
            sharedViewModel.updateTasks(tasks)
            Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error adding task", Toast.LENGTH_SHORT).show()
        }
    }
}