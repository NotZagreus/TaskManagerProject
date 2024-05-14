package com.example.taskmanagerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

// BroadcastReceiver to receive broadcast intents for showing notifications
class NotificationReceiver : BroadcastReceiver() {

    // This method is called when the BroadcastReceiver is receiving an Intent broadcast
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("NotificationReceiver", "onReceive called")
        val taskId = intent.getIntExtra("taskId", 0)
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskDescription = intent.getStringExtra("taskDescription")

        // Show notification to the user
        showNotification(context, taskId, taskTitle, taskDescription)
    }

    // Function to show a notification to the user
    private fun showNotification(context: Context, taskId: Int, taskTitle: String?, taskDescription: String?) {

        Log.d("NotificationReceiver", "showNotification called")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel (required for API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("task_notifications", "Task Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Create notification
        val notificationBuilder = NotificationCompat.Builder(context, "task_notifications")
            .setContentTitle(taskTitle)
            .setContentText(taskDescription)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)

        // Show notification
        notificationManager.notify(taskId, notificationBuilder.build())
    }
}