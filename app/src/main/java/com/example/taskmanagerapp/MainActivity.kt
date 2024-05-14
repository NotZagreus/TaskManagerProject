package com.example.taskmanagerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

// Main activity class that hosts the fragments
class MainActivity : AppCompatActivity(){

    private val CHANNEL_ID = "task_notification_channel"

    // Declare ViewPager and TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewPager and TabLayout
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set the adapter for the ViewPager
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager)


        //TODO delay by 1 second function
        val handler = Handler()
        handler.postDelayed({
            if (true || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Task Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            }
        }, 1000)

    }
}

// Adapter class for providing fragments for the ViewPager
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Return the number of fragments
    override fun getCount(): Int {
        return 2 // Number of fragments
    }

    // Return the Fragment associated with a specified position
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TaskListFragment()
            1 -> AddTaskFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    // Return the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tasks"
            1 -> "Add Task"
            else -> ""
        }
    }
}