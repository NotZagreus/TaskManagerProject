package com.example.taskmanagerapp

import TaskDbHelper
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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

// Fragment for adding tasks
class AddTaskFragment : Fragment() {

    // ViewModel for sharing data between fragments
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })

    // UI elements
    private lateinit var editTextDueTime: EditText
    private lateinit var editTextCreatorName: EditText
    private lateinit var editTextTaskDescription: EditText
    private lateinit var buttonAddTask: Button

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    // Initialize UI elements and set up event listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDueTime = view.findViewById(R.id.editTextDueTime)
        editTextCreatorName = view.findViewById(R.id.editTextCreatorName)
        editTextTaskDescription = view.findViewById(R.id.editTextTaskDescription)
        buttonAddTask = view.findViewById(R.id.buttonAddTask)

        // Show date and time picker when due time field is clicked
        editTextDueTime.setOnClickListener {
            showDateTimePicker()
        }

        // Add task when add task button is clicked
        buttonAddTask.setOnClickListener {
            addTask()
        }
    }

    // Show a date and time picker
    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        // Date picker
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                // Time picker
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        // Set selected date and time
                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)

                        // Format selected date and time
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val formattedDateTime = sdf.format(selectedDateTime.time)

                        // Set formatted date and time in due time field
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

        // Set minimum date to current date
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    // Schedule a notification
    private fun scheduleNotification(taskDescription: String, taskDueTimeMillis: Long) {
        // Create an intent for the notification receiver
        val notificationIntent = Intent(requireContext(), NotificationReceiver::class.java)
        notificationIntent.putExtra("taskDescription", taskDescription)

        // Create a pending intent that will be broadcasted when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule the alarm
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            taskDueTimeMillis - (30 * 60 * 1000), // 30 minutes before task due time
            pendingIntent
        )
    }

    // Add a task
    private fun addTask() {
        // Get current date and time
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        // Get input from fields
        val dueTime = editTextDueTime.text.toString().trim()
        val creatorName = editTextCreatorName.text.toString().trim()
        val taskDescription = editTextTaskDescription.text.toString().trim()

        // Check if all fields are filled
        if (dueTime.isEmpty() || creatorName.isEmpty() || taskDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a helper object to manage the database
        val dbHelper = TaskDbHelper(requireContext())
        // Get a writable database
        val db = dbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_CREATION_TIME, currentDateTime)
            put(TaskContract.TaskEntry.COLUMN_DUE_TIME, dueTime)
            put(TaskContract.TaskEntry.COLUMN_CREATOR_NAME, creatorName)
            put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION, taskDescription)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)

        // Close the database connection
        dbHelper.close()

        // Schedule notification if task is due within 30 minutes
        val taskDueTimeMillis = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dueTime).time
        val currentTimeMillis = System.currentTimeMillis()
        val thirtyMinutesInMillis = 30 * 60 * 1000 // 30 minutes in milliseconds

        if (taskDueTimeMillis - currentTimeMillis <= thirtyMinutesInMillis) {
            scheduleNotification(taskDescription, taskDueTimeMillis)
        }

        // Clear input fields after adding task
        editTextDueTime.text.clear()
        editTextCreatorName.text.clear()
        editTextTaskDescription.text.clear()

        // Check if task was added successfully
        if (newRowId != null && newRowId > -1) {
            // Load tasks from database and update shared view model
            val databaseUtils = DatabaseUtils()
            val tasks = databaseUtils.loadTasksFromDatabase(requireContext())
            sharedViewModel.updateTasks(tasks)
            Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error adding task", Toast.LENGTH_SHORT).show()
        }
    }
}