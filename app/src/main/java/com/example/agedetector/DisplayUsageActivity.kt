package com.example.agedetector

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayUsageActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_usage)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Reference to your TextView in activity_display_usage.xml
        messageTextView = findViewById(R.id.messageTextView)

        // Fetch data from the database
        val usageList = dbHelper.getAllUsages()

        // Display messages in the TextView
        displayMessages(usageList)
    }

    override fun onDestroy() {
        // Close the database when the activity is destroyed
        dbHelper.close()
        super.onDestroy()
    }

    private fun displayMessages(usageList: List<UsageData>) {
        val stringBuilder = StringBuilder()

        for (usageData in usageList) {
            stringBuilder.append("${usageData.username}: ${usageData.date} ${usageData.time}\n")
        }

        // Set the text in the TextView
        messageTextView.text = stringBuilder.toString()
    }
}
